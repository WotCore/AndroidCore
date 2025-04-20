package wot.core.mmkv.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import wot.core.logcat.Logcat
import wot.core.mmkv.utils.ViewBindingUtils

/**
 * @author yangsn
 * @date 2025/2/10
 * @des ViewDataBinding 基类(无ViewModel)
 */
abstract class BaseDataBingFragment<DataBinding : ViewDataBinding> : WotBaseFragment() {

    protected lateinit var mBinding: DataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val constTime = SystemClock.elapsedRealtime()
        val clazz: Class<out BaseDataBingFragment<DataBinding>> = this.javaClass
        if (clazz != BaseDataBingFragment::class.java) {
            ViewBindingUtils.create<DataBinding>(clazz, inflater, container, false)?.let {
                Logcat.run {
                    // 调试各种设备反射 ViewBinding 的性能, 包括加载 Layout
                    d(ViewBindingUtils.TAG, String.format("create ViewBinding: %s, frag=%s", SystemClock.elapsedRealtime() - constTime, this.javaClass.simpleName))
                }
                mBinding = it
                return mBinding.root
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.initView()
    }

    /**
     * 初始化视图
     */
    protected abstract fun DataBinding.initView()

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }
}
