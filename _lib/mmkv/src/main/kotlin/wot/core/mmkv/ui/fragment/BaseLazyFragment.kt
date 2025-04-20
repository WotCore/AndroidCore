package wot.core.mmkv.ui.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import wot.core.mmkv.lifecycle.BaseViewModel

/**
 * @author yangsn
 * @date 2025/2/15
 * @des 懒加载Fragment
 */
abstract class BaseLazyFragment<DataBinding : ViewDataBinding, ViewModel : BaseViewModel>
    : BaseViewModelFragment<DataBinding, ViewModel>(), LifecycleObserver {

    private var mVisibleToUser = false

    override fun onResume() {
        super.onResume()
        if (!mVisibleToUser) {
            mVisibleToUser = true
            lazyLoad()
        }
    }

    /**
     * 懒加载，只有在Fragment第一次创建且第一次对用户可见
     */
    protected abstract fun lazyLoad()
}