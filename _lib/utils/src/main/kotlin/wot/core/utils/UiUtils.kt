package wot.core.utils

import android.app.Activity
import android.view.WindowManager

/**
 * @author yangsn
 * @date 2025/2/20
 * @des
 */
object UiUtils {
    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    fun setPopWindowBackgroundAlpha(activity: Activity, bgAlpha: Float) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha
        if (bgAlpha == 1f) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        activity.window.attributes = lp
    }
}
