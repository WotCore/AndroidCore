package wot.core.utils

import android.app.ActivityManager
import android.content.Context

/**
 * @author yangsn
 * @date 2025/4/12
 * @des 进程工具类
 */
object ProcessUtils {

    /**
     * 获取当前应用的进程名
     */
    fun getCurrentProcessName(context: Context): String? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pid = android.os.Process.myPid()
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

    /**
     * 判断当前进程是否是主进程
     */
    fun isMainProcess(context: Context): Boolean {
        return getCurrentProcessName(context) == context.packageName
    }

    /**
     * 判断当前进程是否为指定的进程
     */
    fun isSpecificProcess(context: Context, processName: String): Boolean {
        return getCurrentProcessName(context) == processName
    }
}
