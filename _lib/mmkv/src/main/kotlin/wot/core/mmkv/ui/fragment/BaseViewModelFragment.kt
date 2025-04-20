package wot.core.mmkv.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import wot.core.mmkv.lifecycle.BaseViewModel

/**
 * @author yangsn
 * @date 2025/2/10
 * @des
 */
abstract class BaseViewModelFragment<DataBinding : ViewDataBinding, ViewModel : BaseViewModel>
    : BaseDataBingFragment<DataBinding>() {

    protected lateinit var mViewModel: ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mViewModel = getViewModel().apply { initViewModelObserve() }
        return view;
    }

    /**
     * 初始化ViewModel
     */
    protected abstract fun getViewModel(): ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.initData()
    }

    /**
     * 初始化数据
     */
    protected abstract fun ViewModel.initData()

    /**
     * 监听当前 ViewModel 中 showDialog和error的值
     */
    private fun BaseViewModel.initViewModelObserve() {
        observeLoadingDialog(this@BaseViewModelFragment) { isShowLoadingDialog ->
            if (isShowLoadingDialog) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }
        observeToastLiveData(this@BaseViewModelFragment) { msg -> msg?.let { showMessage(it) } }
    }
}