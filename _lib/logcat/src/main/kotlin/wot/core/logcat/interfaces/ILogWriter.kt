package wot.core.logcat.interfaces

import wot.core.logcat.mode.LogEntry

/**
 * @author yangsn
 * @date 2025/4/16
 * @des 日志写入器
 */
interface ILogWriter {
    fun write(entry: LogEntry)
}