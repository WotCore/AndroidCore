package wot.core.logcat.mode

/**
 * @author yangsn
 * @date 2025/4/16
 * @des 配置日志系统的选项，包括日志输出、文件写入、崩溃捕获等。
 * @property enableLogWriter 是否启用 Logcat 输出到控制台（默认开启）
 * @property enableFileWriter 是否启用文件写入器（默认开启）（将文件保存到日志文件中）
 * @property enableCrashCatch 是否启用崩溃日志捕获（默认开启）
 * @property maxLogFileSize 最大文件大小（默认 10MB），当日志文件超过该大小时会自动创建新的文件
 * @property maxLogFileCount 最多保留多少个日志文件（默认5个）
 * @property maxBufferSize 缓存的最大日志条数（默认 20 条）
 * @property enabledAutoFlush enabledAutoFlush
 * @property flushIntervalMillis 自动刷新日志的时间间隔，单位：毫秒（默认 2000 毫秒）
 */
data class LogConfig(
    // 日志输出控制
    val enableLogWriter: Boolean = true,  // 是否启用 Logcat 输出到控制台（默认开启）
    val enableFileWriter: Boolean = true,    // 是否启用文件写入器（默认开启）（将文件保存到日志文件中）
    val enableCrashCatch: Boolean = true, // 是否启用崩溃日志捕获（默认开启）

    // 文件控制
    val maxLogFileSize: Long = 10 * 1024 * 1024, // 最大文件大小（默认 10MB），当日志文件超过该大小时会自动创建新的文件
    val maxLogFileCount: Int = 5, // 最多保留多少个日志文件（默认5个）

    // 缓存与刷新控制
    val maxBufferSize: Int = 20,          // 缓存的最大日志条数（默认 20 条）
    val enabledAutoFlush: Boolean = true,     // c
    val flushIntervalMillis: Long = 2000L, // 自动刷新日志的时间间隔，单位：毫秒（默认 2000 毫秒）
)
