package wot.core.logcat.writer

import android.util.Log
import wot.core.logcat.interfaces.ILogWriter
import wot.core.logcat.mode.LogEntry
import wot.core.logcat.mode.LogLevel

/**
 * @author yangsn
 * @date 2025/4/16
 * @des 日志输出器
 */
class LogWriter : ILogWriter {
    override fun write(entry: LogEntry) {
        val level = entry.level
        val tag = entry.tag
        val message = entry.message
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, message)
            LogLevel.DEBUG -> Log.d(tag, message)
            LogLevel.INFO -> Log.i(tag, message)
            LogLevel.WARN -> Log.w(tag, message)
            LogLevel.ERROR -> Log.e(tag, message, entry.throwable)
        }
    }
}