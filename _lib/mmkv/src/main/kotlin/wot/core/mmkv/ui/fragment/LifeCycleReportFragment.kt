package wot.core.mmkv.ui.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import wot.core.logcat.Logcat
import wot.core.logcat.TagRegistry

/**
 * @author yangsn
 * @date 2025/2/10
 * @des Fragment 生命周期记录 (不对外开放)
 */
abstract class LifeCycleReportFragment : Fragment() {

    companion object {

        private const val TAG = "LifeCycleReportFragment"

        private val LOG_ALL_RECYCLE = false /*|| BuildConfig.DEBUG*/
    }


    init {
        TagRegistry.register(TAG)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onAttach: >>> %s <<<", getReportName()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logcat.d(TAG, String.format("onCreate: >>> %s <<<", getReportName()))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onActivityCreated: >>> %s <<<", getReportName()))
        }
    }

    override fun onStart() {
        super.onStart()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onStart: >>> %s <<<", getReportName()))
        }
    }

    override fun onResume() {
        super.onResume()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onResume: >>> %s <<<", getReportName()))
        }
    }

    override fun onPause() {
        super.onPause()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onPause: >>> %s <<<", getReportName()))
        }
    }

    override fun onStop() {
        super.onStop()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onStop: >>> %s <<<", getReportName()))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onSaveInstanceState: >>> %s <<<", getReportName()))
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onViewStateRestored: >>> %s <<<", getReportName()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logcat.d(TAG, String.format("onDestroyView: >>> %s <<<", getReportName()))
    }

    override fun onDetach() {
        super.onDetach()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onDetach: >>> %s <<<", getReportName()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (LOG_ALL_RECYCLE) {
            Logcat.d(TAG, String.format("onDestroy: >>> %s <<<", getReportName()))
        }
    }

    protected open fun getReportName() = this.toString()
}