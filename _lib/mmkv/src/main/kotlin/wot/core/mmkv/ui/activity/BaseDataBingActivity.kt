package wot.core.mmkv.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import wot.core.ui.dialog.LoadingDialog
import wot.core.utils.ToastUtils

/**
 * @author yangsn
 * @date 2025/2/10
 * @des ViewDataBinding 基类(无ViewModel)
 */
abstract class BaseDataBingActivity<DataBinding : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mBinding: DataBinding

    protected var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = initDataBinding(getLayoutId())
        mBinding.initView()
    }

    /**
     * 获取布局资源 id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化 DataBinding
     */
    protected open fun initDataBinding(@LayoutRes layoutId: Int): DataBinding {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    /**
     * 初始化视图
     */
    protected abstract fun DataBinding.initView()

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    /**
     * 显示加载弹窗
     */
    fun showLoadingDialog() {
        if (loadingDialog == null || !loadingDialog!!.isShowing()) {
            loadingDialog = LoadingDialog(this)
            loadingDialog!!.show()
        }
    }

    /**
     * 隐藏加载弹窗
     */
    fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing()) {
                it.dismiss()
            }
        }
        loadingDialog = null
    }

    fun showMessage(msg: String) {
        ToastUtils.showNormalToast(msg)
    }

    fun showMessage(@StringRes resId: Int) {
        ToastUtils.showNormalToast(resId)
    }

    fun showDelayedMessage(@StringRes resId: Int) {
        ToastUtils.showDelayedToast(resId)
    }

    fun showDelayedMessage(msg: String) {
        ToastUtils.showDelayedToast(msg)
    }
}
