package wot.core.ext

import android.os.Bundle
import java.io.Serializable

/**
 * @author yangsn
 * @date 2025/2/10
 * @des Bundle 扩展函数
 */
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? {
    return getSerializable(key) as? T
}