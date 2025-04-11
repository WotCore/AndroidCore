package wot.core.demo.ui.livedata

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import wot.android.core.livedata.FlexibleLiveData
import wot.core.demo.R

/**
 * @author yangsn
 * @date 2025/4/11
 * @des
 */
// 创建 FlexibleLiveData 实例
private val flexibleLiveData = FlexibleLiveData<String>()

class FlexibleLiveDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flexible_livedata_activity)

        // 非粘性观察者
        flexibleLiveData.observe(this) { value ->
            Log.d("xxa", "非粘性观察者收到更新: $value")
        }

        // 粘性观察者
        flexibleLiveData.observeSticky(this) { value ->
            Log.d("xxa", "粘性观察者收到更新: $value")
        }

        findViewById<View>(R.id.value).setOnClickListener {
            flexibleLiveData.value = "Hello, World!"
        }

        findViewById<View>(R.id.postValue).setOnClickListener {
            flexibleLiveData.postValue("Hello from postValue!")
        }
    }
}