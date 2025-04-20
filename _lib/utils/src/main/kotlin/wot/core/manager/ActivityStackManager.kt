package wot.core.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import wot.core.logcat.Logcat
import wot.core.logcat.TagRegistry
import java.util.Stack
import java.util.concurrent.CopyOnWriteArraySet

/**
 * @author yangsn
 * @date 2025/3/22
 * @des Activity 栈堆管理
 */
class ActivityStackManager private constructor() : Application.ActivityLifecycleCallbacks {

    private val activityStack = Stack<Activity>()
    private val listenerSet = CopyOnWriteArraySet<(Boolean) -> Unit>() // 线程安全的监听集合

    @Volatile
    private var foregroundCount = 0

    @Volatile
    private var isAppInBackground = true

    companion object {
        private const val TAG = "ActivityStackManager"
        private val instance: ActivityStackManager by lazy { ActivityStackManager() }
        fun get() = instance
    }

    init {
        TagRegistry.register(TAG)
    }

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
        Logcat.d(TAG, "ActivityStackManager 初始化完成")
    }

    /**
     * 添加前后台切换监听
     */
    fun addAppStateListener(listener: (Boolean) -> Unit) {
        listenerSet.add(listener)
    }

    /**
     * 移除前后台切换监听
     */
    fun removeAppStateListener(listener: (Boolean) -> Unit) {
        listenerSet.remove(listener)
    }

    /**
     * 获取当前栈顶Activity
     */
    fun getTopActivity(): Activity? = synchronized(activityStack) {
        if (activityStack.isEmpty()) null else activityStack.peek()
    }

    /**
     * 关闭所有Activity
     */
    fun finishAllActivities() {
        synchronized(activityStack) {
            while (activityStack.isNotEmpty()) {
                val activity = activityStack.pop()
                Logcat.d(TAG, "关闭 Activity: ${getActivityName(activity)}")
                activity.finish()
            }
        }
    }

    /**
     * 当前应用是否在前台
     */
    fun isAppForeground(): Boolean = !isAppInBackground

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        synchronized(activityStack) {
            activityStack.push(activity)
        }
        Logcat.d(
            TAG,
            "onCreate: ${getActivityName(activity)}, 当前栈大小: ${activityStack.size}"
        )
    }

    override fun onActivityStarted(activity: Activity) {
        synchronized(this) {
            foregroundCount++
            if (isAppInBackground) {
                isAppInBackground = false
                Logcat.d(TAG, "应用回到前台")
                listenerSet.forEach { it(true) } // 触发所有监听器
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        synchronized(this) {
            foregroundCount--
            if (foregroundCount == 0) {
                isAppInBackground = true
                Logcat.d(TAG, "应用进入后台")
                listenerSet.forEach { it(false) } // 触发所有监听器
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        synchronized(activityStack) {
            activityStack.remove(activity)
        }
        Logcat.d(
            TAG,
            "onDestroy: ${getActivityName(activity)}, 当前栈大小: ${activityStack.size}"
        )
    }

    /**
     * 使用反射获取Activity的完整类名，避免混淆影响
     */
    private fun getActivityName(activity: Activity): String {
        return activity::class.java.name
    }
}

