package wot.core.app

import android.app.Application
import android.content.Context
import wot.core.logcat.Logcat
import wot.core.logcat.mode.LogConfig

/**
 * @author yangsn
 * @date 2025/4/12
 * @des 继承自 Application
 */

val application: Application by lazy { BaseApplication.instance }
val context: Context by lazy { application.applicationContext }

open class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        Logcat.init(this, LogConfig())
    }

    override fun onTerminate() {
        super.onTerminate()
        Logcat.shutdown()
    }
}

