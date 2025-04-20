package wot.core.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import wot.core.ui.R
import wot.core.utils.ToastUtils

/**
 * @author yangsn
 * @date 2025/4/20
 * @des 加载进度弹窗
 */
class LoadingDialog(activity: Activity?, private val timeOutMsg: String = "网络错误") {

    private var loadingDialog: Dialog? = null
    private var runnable: Runnable? = null
    private var delayRunnable: Runnable? = null
    private var view: View? = null
    private var handler: Handler? = null
    private var timeOut: Long = 30000

    init {
        init(activity)
        initTask()
    }

    /**
     * 初始化弹窗
     */
    private fun init(context: Activity?) {
        context?.let {
            // 首先得到整个View
            view = LayoutInflater.from(it).inflate(R.layout.lib_ui_loading_dialog, null)
            loadingDialog = Dialog(it, R.style.Lib_Ui_LoadingDialogTheme).apply {
                window?.setDimAmount(0f)
            }
            handler = Handler()
            setCanceledOnTouchOutside(true)
        } ?: run {
            throw IllegalArgumentException("Activity cannot be null")
        }
    }

    /**
     * 初始化任务，超时与延迟显示任务
     */
    private fun initTask() {
        runnable = Runnable { timeoutCancel() }
        delayRunnable = Runnable { showLoading() }
    }

    /**
     * 显示dialog
     */
    fun show() {
        handler?.postDelayed(delayRunnable!!, 50)
    }

    /**
     * 显示加载框
     */
    private fun showLoading() {
        loadingDialog?.apply {
            if (!isShowing) {
                show()
                handler?.postDelayed(runnable!!, timeOut)
            }
        }
    }

    /**
     * 隐藏弹窗
     */
    fun dismiss() {
        if (Thread.currentThread().id == Process.myTid().toLong()) {
            loadingDialog?.dismiss()
        } else {
            handler?.post {
                loadingDialog?.dismiss()
            }
        }
        handler?.removeCallbacks(runnable!!)
        handler?.removeCallbacks(delayRunnable!!)
    }

    /**
     * 超时处理
     */
    private fun timeoutCancel() {
        loadingDialog?.takeIf { it.isShowing }?.apply {
            dismiss()
            ToastUtils.showNormalToast(timeOutMsg)
        }
    }

    /**
     * 设置超时
     */
    fun setTimeOut(timeOut: Long) {
        this.timeOut = timeOut
    }

    /**
     * 设置返回键无效
     */
    fun setCanceledOnTouchOutside(isClick: Boolean) {
        loadingDialog?.apply {
            setCanceledOnTouchOutside(isClick)
            setContentView(
                view ?: throw IllegalStateException("View cannot be null"),
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    /**
     * 销毁
     */
    fun onDestroy() {
        loadingDialog?.dismiss()
        handler?.removeCallbacks(runnable!!)
    }

    /**
     * 是否显示
     */
    fun isShowing(): Boolean {
        return loadingDialog?.isShowing == true
    }
}
