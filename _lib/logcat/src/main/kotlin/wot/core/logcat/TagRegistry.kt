package wot.core.logcat

/**
 * @author yangsn
 * @date 2025/4/18
 * @des tag 注册管理
 */
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object TagRegistry {
    // 使用 ReentrantLock 提供更细粒度的锁控制
    private val lock = ReentrantLock()
    private val tagSet: MutableSet<String> = mutableSetOf()

    // 注册 tag，避免重复注册
    fun register(tag: String) {
        lock.withLock {
            if (!tagSet.contains(tag)) {
                tagSet.add(tag)
            }
        }
    }

    // 注销 tag
    fun unregister(tag: String) {
        lock.withLock {
            tagSet.remove(tag)
        }
    }

    // 清空所有 tag
    fun clear() {
        lock.withLock {
            tagSet.clear()
        }
    }

    // 判断 tag 是否已注册
    fun isTagRegistered(tag: String): Boolean {
        lock.withLock {
            return tagSet.contains(tag)
        }
    }
}
