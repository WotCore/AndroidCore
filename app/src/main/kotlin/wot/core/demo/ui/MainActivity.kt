package wot.core.demo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import wot.core.demo.R
import wot.core.demo.ui.livedata.FlexibleLiveDataActivity

/**
 * @author yangsn
 * @date 2025/4/11
 * @des
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<View>(R.id.FlexibleLiveData).setOnClickListener {
            startActivity(Intent(this, FlexibleLiveDataActivity::class.java))
        }
    }
}