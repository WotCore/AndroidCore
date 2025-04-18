package wot.core.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * @author yangsn
 * @date 2025/2/11
 * @des Activity 扩展函数
 */
inline fun <reified T : Activity> Context.start() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}