package wot.core.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * @author yangsn
 * @date 2025/2/17
 * @des 防双击
 */
object ClickUtils {
    private const val DEFAULT_DELAY: Long = 200

    private val lastClickMap = ConcurrentHashMap<String, Long>()

    fun preventDoubleClick(action: () -> Unit) {
        preventDoubleClick("lazy", action)
    }

    fun preventDoubleClick(tag: String, action: () -> Unit) {
        preventDoubleClick(tag, DEFAULT_DELAY, action)
    }

    /**
     * 防止相同标识的操作在指定时间内重复执行
     * @param tag 操作标识，相同标识共享时间
     * @param delayMillis 时间间隔（毫秒）
     * @param action 需要执行的操作
     */
    fun preventDoubleClick(tag: String, delayMillis: Long = DEFAULT_DELAY, action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        val lastClickTime = lastClickMap[tag] ?: 0L

        if (currentTime - lastClickTime >= delayMillis) {
            lastClickMap[tag] = currentTime
            action.invoke()
        }
    }

    /**
     * 防止相同标识的操作在指定时间内重复执行
     * @param tag 操作标识，相同标识共享时间
     * @param delayMillis 时间间隔（毫秒）
     * @return true: 可点击, false: 不可点击
     */
    fun checkClick(tag: String, delayMillis: Long = DEFAULT_DELAY): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastClickTime = lastClickMap[tag] ?: 0L

        if (currentTime - lastClickTime >= delayMillis) {
            lastClickMap[tag] = currentTime
            return true
        }
        return false
    }
}
