package wot.core.mmkv.ui.fragment

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import wot.core.mmkv.ui.activity.BaseDataBingActivity
import wot.core.utils.ToastUtils

/**
 * @author yangsn
 * @date 2025/2/10
 * @des 基类 Fragment (不对外开放)
 */
abstract class WotBaseFragment : LifeCycleReportFragment() {

    protected lateinit var mActivity: BaseDataBingActivity<*>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseDataBingActivity<*>
    }

    /**
     * 显示加载弹窗
     */
    fun showLoadingDialog() {
        mActivity.showLoadingDialog()
    }

    /**
     * 隐藏加载弹窗
     */
    fun dismissLoadingDialog() {
        mActivity.dismissLoadingDialog()
    }

    fun showMessage(@StringRes resId: Int) {
        ToastUtils.showNormalToast(resId)
    }

    fun showMessage(msg: String) {
        ToastUtils.showNormalToast(msg)
    }

    fun showDelayedMessage(@StringRes resId: Int) {
        ToastUtils.showDelayedToast(resId)
    }

    fun showDelayedMessage(msg: String) {
        ToastUtils.showDelayedToast(msg)
    }

    protected fun <T> activityCast(): BaseDataBingActivity<*> {
        return mActivity
    }

    protected fun finishActivity(activity: Activity? = mActivity) {
        activity?.finish()
    }

    protected fun finishAfterTransition(activity: Activity? = mActivity) {
        activity?.finishAfterTransition()
    }
}
