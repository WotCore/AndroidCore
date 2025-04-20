package wot.core.mmkv.ui.popup_window

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import wot.core.mmkv.R
import wot.core.utils.DeviceUtils
import wot.core.utils.UiUtils

/**
 * @author yangsn
 * @date 2025/2/20
 * @des
 */
abstract class BasePopupWindow<DataBinding : ViewDataBinding?>(mActivity: Activity) :
    PopupWindow(mActivity),
    PopupWindow.OnDismissListener {
    protected var mRootView: View
    protected var mActivity: Activity?
    private var mDataBinding: DataBinding? = null
    protected var dismissListener: OnPopDismissListener? = null

    protected val binding: DataBinding
        get() = mDataBinding!!

    init {
        this.mActivity = mActivity
        val inflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mRootView = inflater.inflate(getLayoutId(), null)
        this.contentView = mRootView
        initBase()
        initView()
        initListener()
    }

    override fun setContentView(contentView: View?) {
        super.setContentView(contentView)
        mDataBinding = DataBindingUtil.bind(contentView!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        // 单击弹出窗以外处 关闭弹出窗
        mRootView.setOnTouchListener { v: View?, event: MotionEvent ->
            getTopView()?.let {
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.y < it.top || event.y > it.bottom || event.x < it.left || event.x > it.right
                    ) {
                        dismiss()
                    }
                }
            }
            true
        }
        setOnDismissListener(this)
    }

    private fun initBase() {
        // 设置动画效果
        this.animationStyle = animationStyle
        this.width = width
        this.height = height
        // 设置可触
        this.isFocusable = isFocusable
        setBackgroundDrawable(background)
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun getTopView(): View?

    protected abstract fun initView()

    override fun getAnimationStyle(): Int {
        return R.style.Lib_MVVM_BasePopupWindowAnimation
    }

    override fun getBackground(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    override fun getHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun getWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun isFocusable(): Boolean {
        return true
    }

    override fun onDismiss() {
        mActivity?.let { UiUtils.setPopWindowBackgroundAlpha(it, 1f) }
        if (dismissListener != null) {
            dismissListener!!.onPopDismiss()
        }
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        showAtLocation(parent, gravity, x, y, 0.4f)
    }

    fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int, bgAlpha: Float) {
        if (mActivity == null || mActivity!!.isDestroyed) {
            return
        }
        UiUtils.setPopWindowBackgroundAlpha(mActivity!!, bgAlpha)
        super.showAtLocation(parent, gravity, x, y)
    }

    /**
     * 默认显示在 anchorView 下方，当屏幕不足以显示弹窗时，显示才会显示在上方
     *
     * @param anchorView 目标控件
     */
    fun showAutoLocation(anchorView: View) {
        showAtLocation(anchorView, Gravity.CENTER, java.lang.Boolean.TRUE)
    }

    /**
     * 始终显示在下方
     */
    fun showAlwaysBelow(anchor: View, hGravity: Int) {
        showAtLocation(anchor, hGravity, java.lang.Boolean.FALSE)
    }

    /**
     * @param anchorView 目标控件
     * @param hGravity   方向
     * @param isAuto     true 时智能调节, false 时始终显示在下方
     */
    fun showAtLocation(anchorView: View, hGravity: Int, isAuto: Boolean) {
        mRootView.measure(0, 0)
        val height = mRootView.measuredHeight
        val width = mRootView.measuredWidth
        setWidth(width)
        setHeight(height)
        val outLocation = intArrayOf(0, 0)
        anchorView.getLocationInWindow(outLocation)
        val anchorMeasuredHeight = anchorView.measuredHeight
        val anchorMeasuredWidth = anchorView.measuredWidth
        val screenHeight: Int = DeviceUtils.getScreenHeight(mActivity!!)
        val xOffSet: Int
        xOffSet = when (hGravity) {
            Gravity.START, Gravity.LEFT -> {
                outLocation[0]
            }

            Gravity.END, Gravity.RIGHT -> {
                outLocation[0] + anchorMeasuredWidth - width
            }

            Gravity.CENTER -> {
                outLocation[0] + (anchorMeasuredWidth - width) / 2
            }

            else -> {
                outLocation[0] + (anchorMeasuredWidth - width) / 2
            }
        }
        val yOffSet: Int
        yOffSet = if (isAuto) {
            val isDown = outLocation[1] + anchorMeasuredHeight + height < screenHeight
            if (isDown) {
                outLocation[1] + anchorMeasuredHeight
            } else {
                outLocation[1] - height
            }
        } else {
            outLocation[1] + anchorMeasuredHeight
        }
        super.showAtLocation(
            anchorView.parent as View,
            (Gravity.TOP or Gravity.LEFT),
            xOffSet,
            yOffSet
        )
    }

    fun setOnPopDismissListener(onPopDismissListener: OnPopDismissListener?) {
        this.dismissListener = onPopDismissListener
    }

    interface OnPopDismissListener {
        fun onPopDismiss()
    }
}