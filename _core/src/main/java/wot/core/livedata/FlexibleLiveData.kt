package wot.core.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicLong

/**
 * @author yangsn
 * @date 2025/4/11
 * @des 支持非粘性和粘性事件的 LiveData 实现。
 * - 非粘性观察者：只会在 setValue 或 postValue 被调用时收到更新。
 * - 粘性观察者：在注册时，立即收到当前的值，即使数据没有变化。
 */
class FlexibleLiveData<T> : MutableLiveData<T>() {

    private val currentVersion = AtomicLong(START_VERSION)
    private val observerMap = mutableMapOf<Observer<in T>, ObserverWrapper>()

    companion object {
        private const val START_VERSION = -1L
    }

    override fun setValue(value: T?) {
        currentVersion.incrementAndGet()
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        currentVersion.incrementAndGet()
        super.postValue(value)
    }

    /**
     * 非粘性观察者
     * 通过 observe 方法注册的观察者，只有在数据被更新后（通过 setValue 或 postValue）才会收到通知。
     * 这对于只关心数据变化的场景非常有用。
     */
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer, currentVersion.get())
        observerMap[observer] = wrapper
        super.observe(owner, wrapper)
    }

    /**
     * 粘性观察者
     * 在注册时立即收到当前的 LiveData 值。
     * 这对于一些需要在初始化时获取当前状态的场景非常有用，比如页面加载时获取数据的情况。
     */
    fun observeSticky(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observerMap[observer] = wrapper
        super.observe(owner, wrapper)
    }

    /**
     * 自动识别包装过的 Observer 进行解绑
     */
    override fun removeObserver(observer: Observer<in T>) {
        observerMap.remove(observer)?.let {
            super.removeObserver(it)
        } ?: super.removeObserver(observer)
    }

    /**
     * 清空当前数据与版本，可用于重置
     */
    fun clear() {
        currentVersion.set(START_VERSION)
        super.setValue(null)
    }

    private inner class ObserverWrapper(
        private val observer: Observer<in T>,
        private val observerVersion: Long = START_VERSION
    ) : Observer<T> {

        override fun onChanged(t: T?) {
            // 如果当前版本号大于 observer 的版本号，则更新 observer
            if (currentVersion.get() > observerVersion) {
                observer.onChanged(t)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FlexibleLiveData<*>.ObserverWrapper) return false
            return observer == other.observer
        }

        override fun hashCode(): Int {
            return observer.hashCode()
        }
    }
}
