package wot.core.mmkv.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wot.core.logcat.Logcat
import wot.core.logcat.TagRegistry

/**
 * @author yangsn
 * @date 2025/2/10
 * @des
 */
abstract class BaseViewModel : ViewModel() {

    companion object {

        private const val TAG = "BaseViewModel"
    }

    init {
        TagRegistry.register(TAG)
    }

    /**
     * 用来通知 Activity／Fragment 是否显示等待Dialog
     */
    protected val loadingDialogLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    /**
     * 土司
     */
    protected val toastLiveData by lazy { MutableLiveData<String>() }

    fun observeLoadingDialog(owner: LifecycleOwner?, observer: Observer<Boolean>?) {
        loadingDialogLiveData.observe(owner!!, observer!!)
    }

    fun observeToastLiveData(owner: LifecycleOwner?, observer: Observer<String?>) {
        toastLiveData.observe(owner!!, observer)
    }

    fun io(
        block: suspend () -> Unit,
        onError: (suspend (Exception) -> Unit)? = null // 这里可以做 UI 反馈或日志上报
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { block() }
                .onFailure { e ->
                    Logcat.e(TAG, "io-> 发生异常: ${e.message}")
                    onError?.invoke(e as Exception)
                }
        }
    }

    fun ui(
        block: suspend () -> Unit,
        onError: (suspend (Exception) -> Unit)? = null // 这里可以做 UI 反馈或日志上报
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            runCatching { block() }
                .onFailure { e ->
                    Logcat.e(TAG, "ui-> 发生异常: ${e.message}")
                    onError?.invoke(e as Exception)
                }
        }
    }
}