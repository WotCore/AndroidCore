package wot.core.logcat.mode

/**
 * @author yangsn
 * @date 2025/4/16
 * @des 日志信息
 */
data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
    val timestamp: Long = System.currentTimeMillis()
)