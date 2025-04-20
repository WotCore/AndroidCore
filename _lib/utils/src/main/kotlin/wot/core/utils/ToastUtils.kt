package wot.core.utils

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import wot.core.ext.R

/**
 * @author yangsn
 * @date 2025/4/20
 * @des toast 工具
 */
object ToastUtils {

    private lateinit var application: Application
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var toast: Toast? = null

    @Synchronized
    fun inject(app: Application) {
        application = app
    }

    /**
     * 显示普通吐司（立即执行）
     */
    fun showNormalToast(
        @StringRes resId: Int,
        duration: Int = Toast.LENGTH_LONG,
        gravity: Int = Gravity.CENTER
    ) {
        if (::application.isInitialized) {
            showNormalToast(application.getString(resId), duration, gravity)
        }
    }

    /**
     * 显示普通吐司（立即执行）
     */
    fun showNormalToast(
        message: String,
        duration: Int = Toast.LENGTH_LONG,
        gravity: Int = Gravity.CENTER
    ) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            makeToast(message, duration, gravity)
        } else {
            handler.post { makeToast(message, duration, gravity) }
        }
    }

    /**
     * 延迟显示吐司（默认延迟 300ms）
     */
    fun showDelayedToast(
        @StringRes resId: Int,
        delayMillis: Long = 300,
        duration: Int = Toast.LENGTH_LONG,
        gravity: Int = Gravity.CENTER
    ) {
        if (::application.isInitialized) {
            showDelayedToast(application.getString(resId), delayMillis, duration, gravity)
        }
    }

    /**
     * 延迟显示吐司（默认延迟 300ms）
     */
    fun showDelayedToast(
        message: String,
        delayMillis: Long = 300,
        duration: Int = Toast.LENGTH_LONG,
        gravity: Int = Gravity.CENTER
    ) {
        handler.postDelayed({ makeToast(message, duration, gravity) }, delayMillis)
    }

    /**
     * 构造并展示自定义 Toast
     */
    @Synchronized
    private fun makeToast(msg: String, duration: Int, gravity: Int) {
        if (!::application.isInitialized) return

        toast?.cancel()

        val view = View.inflate(application, R.layout.lib_utils_toast_view, null)
        val toastTextView = view.findViewById<TextView>(R.id.toast_view_text)
        toastTextView.text = msg

        toast = Toast(application).apply {
            this.duration = duration
            setGravity(gravity, 0, 0)
            this.view = view
            show()
        }
    }

    /**
     * 主动取消当前 Toast（如有）
     */
    @Synchronized
    fun cancel() {
        toast?.cancel()
        toast = null
    }
}
