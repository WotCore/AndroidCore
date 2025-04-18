package wot.core.logcat.writer

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import wot.core.logcat.TagRegistry
import wot.core.logcat.interfaces.ILogWriter
import wot.core.logcat.mode.LogConfig
import wot.core.logcat.mode.LogEntry
import java.io.File
import java.io.Flushable
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author yangsn
 * @date 2025/4/16
 * @des 文件写入器
 */
class FileLogWriter(
    context: Context,
    private val config: LogConfig
) : ILogWriter, Flushable {

    private val logDir: File
    private var logFile: File
    private val buffer = ConcurrentLinkedQueue<String>()
    private val flushScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // 使用应用内部存储的默认文件夹
        logDir = File(context.filesDir, "logcat_logs").apply { mkdirs() }

        logFile = findOrCreateLogFile()

        if (config.enabledAutoFlush) {
            flushScope.launch {
                while (isActive) {
                    delay(config.flushIntervalMillis)
                    flush()
                }
            }
        }
    }

    private fun findOrCreateLogFile(): File {
        val logFiles = logDir
            .listFiles { file ->
                file.isFile && file.name.matches(Regex("log_\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}\\.txt"))
            }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()

        val latest = logFiles.firstOrNull()

        if (latest != null && latest.length() < config.maxLogFileSize) {
            return latest
        }

        return createNewLogFile()
    }

    override fun write(entry: LogEntry) {
        // 只处理注册过的 tag
        if (!TagRegistry.isTagRegistered(entry.tag)) return

        val log = format(entry)
        buffer.offer(log)

        if (buffer.size >= config.maxBufferSize) {
            flush()
        }
    }

    override fun flush() {
        if (buffer.isEmpty()) return

        val logs = mutableListOf<String>()
        while (true) {
            val line = buffer.poll() ?: break
            logs.add(line)
        }

        if (logs.isEmpty()) return

        writeToFileWithLock(logs.joinToString(""))
    }

    private fun writeToFileWithLock(content: String) {
        var lock: FileLock? = null
        var raf: RandomAccessFile? = null

        try {
            raf = RandomAccessFile(logFile, "rw")
            val channel = raf.channel
            lock = channel.lock()
            channel.position(channel.size())
            channel.write(java.nio.ByteBuffer.wrap(content.toByteArray()))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                lock?.release()
                raf?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (logFile.length() >= config.maxLogFileSize) {
            rotateLogFile()
        }
    }

    private fun rotateLogFile() {
        val newFile = File(logDir, "log_${currentTimestamp()}.txt")
        if (logFile.renameTo(newFile)) {
            logFile = createNewLogFile()
            cleanOldLogFiles()
        }
    }

    private fun cleanOldLogFiles() {
        val logFiles = logDir
            .listFiles { file ->
                file.isFile && file.name.matches(Regex("log_\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}\\.txt"))
            }
            ?.sortedByDescending { it.lastModified() }
            ?: return

        if (logFiles.size <= config.maxLogFileCount) return

        logFiles.drop(config.maxLogFileCount).forEach { it.delete() }
    }

    private fun createNewLogFile(): File {
        val newFile = File(logDir, "log_${currentTimestamp()}.txt")
        if (!newFile.exists()) newFile.createNewFile()
        return newFile
    }

    private fun currentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
    }

    private fun format(event: LogEntry): String {
        val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(Date(event.timestamp))
        val throwable =
            event.throwable?.let { "\n${android.util.Log.getStackTraceString(it)}" } ?: ""
        return "$time ${event.level.name}/${event.tag}: ${event.message}$throwable\n"
    }
}
