package wot.core.mmkv.ui.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import wot.core.mmkv.lifecycle.BaseViewModel

/**
 * @author yangsn
 * @date 2025/2/10
 * @des
 */
abstract class BaseViewModelActivity<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> :
    BaseDataBingActivity<DataBinding>() {

    protected lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.initData()
    }

    /**
     * 初始化 ViewModel
     */
    override fun initDataBinding(layoutId: Int): DataBinding {
        val dataBinding: DataBinding = super.initDataBinding(layoutId)
        mViewModel = getViewModel()
        initObserve()
        return dataBinding
    }

    /**
     * 初始化 ViewModel
     */
    protected abstract fun getViewModel(): ViewModel

    /**
     * 初始化数据
     */
    protected abstract fun ViewModel.initData()

    /**
     * 监听当前ViewModel中 showDialog和error的值
     */
    private fun initObserve() {
        mViewModel.apply {
            observeLoadingDialog(this@BaseViewModelActivity) { bean ->
                if (bean) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            }
            observeToastLiveData(this@BaseViewModelActivity) { msg -> showMessage(msg!!) }
        }
    }
}