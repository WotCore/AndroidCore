package wot.core.logcat

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wot.core.logcat.writer.FileLogWriter
import wot.core.logcat.writer.LogWriter
import wot.core.logcat.interfaces.ILogWriter
import wot.core.logcat.mode.LogEntry
import wot.core.logcat.mode.LogLevel
import wot.core.logcat.mode.LogConfig
import java.io.Flushable
import java.util.concurrent.CopyOnWriteArrayList

object Logcat {

    private val logWriterList = CopyOnWriteArrayList<ILogWriter>()
    private val eventChannel = Channel<LogEntry>(Channel.UNLIMITED)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var isShutdown = false

    init {
        scope.launch {
            for (event in eventChannel) {
                logWriterList.forEach { it.write(event) }
            }
        }
    }

    fun init(context: Context, config: LogConfig = LogConfig()) {
        if (config.enableLogWriter) addWriter(LogWriter())
        if (config.enableFileWriter) addWriter(FileLogWriter(context, config))
        if (config.enableCrashCatch) enableUncaughtExceptionHandler()
    }

    /**
     * 添加 日志写入器
     */
    fun addWriter(logWriter: ILogWriter) {
        if (!isShutdown) logWriterList.add(logWriter)
    }

    /**
     * 日志输出
     */
    fun log(
        level: LogLevel,
        tag: String,
        message: String,
        throwable: Throwable? = null,
        timestamp: Long = System.currentTimeMillis()
    ) {
        if (isShutdown) return
        val event = LogEntry(level, tag, message, throwable, timestamp)
        eventChannel.trySend(event)
    }

    fun v(tag: String, msg: String) = log(LogLevel.VERBOSE, tag, msg)
    fun d(tag: String, msg: String) = log(LogLevel.DEBUG, tag, msg)
    fun i(tag: String, msg: String) = log(LogLevel.INFO, tag, msg)
    fun w(tag: String, msg: String) = log(LogLevel.WARN, tag, msg)
    fun e(tag: String, msg: String, tr: Throwable? = null) = log(LogLevel.ERROR, tag, msg, tr)

    fun shutdown(timeoutMillis: Long = 3000) {
        if (isShutdown) return
        isShutdown = true
        eventChannel.close()
        CoroutineScope(Dispatchers.IO).launch {
            delay(timeoutMillis)
            scope.cancel()
        }
    }

    /**
     * 启用崩溃日志捕获
     */
    private fun enableUncaughtExceptionHandler() {
        val handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            e("UncaughtException", "崩溃线程: ${thread.name}", throwable)
            flush()
            shutdown()
            handler?.uncaughtException(thread, throwable)
        }
    }

    fun flush() {
        logWriterList.forEach {
            if (it is Flushable) {
                it.flush()
            }
        }
    }
}
