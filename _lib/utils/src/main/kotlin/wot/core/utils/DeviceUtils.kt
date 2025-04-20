package wot.core.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @author yangsn
 * @date 2025/2/20
 * @des
 */
object DeviceUtils {

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(activity: Activity): Int {
        val outMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(activity: Activity): Int {
        val outMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 关闭键盘输入法
     */
    fun hideSoftInput(context: Context, v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 显示键盘输入法
     */
    fun showSoftInput(context: Context, v: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }

    /**
     * 网络是否连接
     *
     * @return
     */
    fun isNetworkConnected(context: Context): Boolean {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        return mNetworkInfo?.isAvailable ?: false
    }
}
