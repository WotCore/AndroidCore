package wot.core.ext

import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.io.Serializable

/**
 * Bundle 扩展函数
 *
 * @author yangsn
 * @date 2025/2/10
 */

val bundleGson by lazy { Gson() }

fun bundleOfSafe(vararg pairs: Pair<String, Any?>): Bundle {
    val bundle = Bundle()
    for ((key, value) in pairs) {
        when (value) {
            null -> bundle.putString(key, null)

            // 基本类型
            is Int -> bundle.putInt(key, value)
            is Long -> bundle.putLong(key, value)
            is String -> bundle.putString(key, value)
            is Boolean -> bundle.putBoolean(key, value)
            is Float -> bundle.putFloat(key, value)
            is Double -> bundle.putDouble(key, value)
            is Char -> bundle.putChar(key, value)
            is Short -> bundle.putShort(key, value)
            is Byte -> bundle.putByte(key, value)
            is CharSequence -> bundle.putCharSequence(key, value)

            // 数组类型
            is IntArray -> bundle.putIntArray(key, value)
            is LongArray -> bundle.putLongArray(key, value)
            is FloatArray -> bundle.putFloatArray(key, value)
            is DoubleArray -> bundle.putDoubleArray(key, value)
            is CharArray -> bundle.putCharArray(key, value)
            is ShortArray -> bundle.putShortArray(key, value)
            is ByteArray -> bundle.putByteArray(key, value)
            is BooleanArray -> bundle.putBooleanArray(key, value)

            is Array<*> -> {
                when {
                    value.isArrayOf<String>() -> bundle.putStringArray(key, value as Array<String>)
                    value.isArrayOf<CharSequence>() -> bundle.putCharSequenceArray(
                        key,
                        value as Array<CharSequence>
                    )

                    value.isArrayOf<Parcelable>() -> bundle.putParcelableArray(
                        key,
                        value as Array<Parcelable>
                    )

                    else -> throw IllegalArgumentException("不支持的 Array 类型 \"$key\"")
                }
            }

            // 列表类型
            is ArrayList<*> -> {
                when {
                    value.all { it is Parcelable } -> bundle.putParcelableArrayList(
                        key,
                        value as ArrayList<Parcelable>
                    )

                    value.all { it is String } -> bundle.putStringArrayList(
                        key,
                        value as ArrayList<String>
                    )

                    value.all { it is CharSequence } -> bundle.putCharSequenceArrayList(
                        key,
                        value as ArrayList<CharSequence>
                    )

                    value.all { it is Int } -> bundle.putIntegerArrayList(
                        key,
                        value as ArrayList<Int>
                    )

                    else -> throw IllegalArgumentException("不支持的 ArrayList 类型 \"$key\"")
                }
            }

            // Parcelable / Serializable / Enum
            is Parcelable -> bundle.putParcelable(key, value)
            is Serializable -> bundle.putSerializable(key, value)
            is Enum<*> -> bundle.putSerializable(key, value)

            // 嵌套 Bundle
            is Bundle -> bundle.putBundle(key, value)

            // 非 Parcelable 对象通过 JSON 存储
            else -> {
                val json = bundleGson.toJson(value)
                bundle.putString("__json_$key", json)
                bundle.putString("__json_class_$key", value::class.java.name)
            }
        }
    }

    // Debug 模式下检测大小
    if (BuildConfig.DEBUG) checkBundleSize(bundle)

    return bundle
}

private fun checkBundleSize(bundle: Bundle) {
    try {
        val parcel = Parcel.obtain()
        bundle.writeToParcel(parcel, 0)
        val size = parcel.dataSize()
        parcel.recycle()
        if (size > 500 * 1024) {
            Log.w(
                "SafeBundle",
                "⚠️ Bundle 可能过大（${size / 1024}KB），请优化参数传递，避免 TransactionTooLargeException"
            )
        }
    } catch (e: Exception) {
        Log.e("SafeBundle", "检测 Bundle 大小时异常：${e.message}")
    }
}

// -------------------- Fragment 扩展 --------------------

fun <T : Fragment> T.withArgs(vararg pairs: Pair<String, Any?>): T {
    this.arguments = bundleOfSafe(*pairs)
    return this
}

inline fun <reified T> Fragment.arg(key: String): T? {
    return arguments?.safeGet<T>(key)
}

inline fun <reified T> Fragment.requireArg(key: String): T {
    return arg<T>(key) ?: error("参数 \"$key\" 不存在或类型错误")
}

inline fun <reified T : Parcelable> Fragment.argList(key: String): ArrayList<T>? {
    return arguments?.getParcelableArrayList(key)
}

// -------------------- Bundle 扩展 --------------------

inline fun <reified T> Bundle.safeGet(key: String): T? {
    if (containsKey(key)) {
        return when (T::class) {
            Int::class -> getInt(key) as T
            Long::class -> getLong(key) as T
            String::class -> getString(key) as T
            Boolean::class -> getBoolean(key) as T
            Float::class -> getFloat(key) as T
            Double::class -> getDouble(key) as T
            Char::class -> getChar(key) as T
            Short::class -> getShort(key) as T
            Byte::class -> getByte(key) as T
            CharSequence::class -> getCharSequence(key) as T

            IntArray::class -> getIntArray(key) as T
            LongArray::class -> getLongArray(key) as T
            FloatArray::class -> getFloatArray(key) as T
            DoubleArray::class -> getDoubleArray(key) as T
            CharArray::class -> getCharArray(key) as T
            ShortArray::class -> getShortArray(key) as T
            ByteArray::class -> getByteArray(key) as T
            BooleanArray::class -> getBooleanArray(key) as T
            Array<String>::class -> getStringArray(key) as T
            Array<CharSequence>::class -> getCharSequenceArray(key) as T
            Array<Parcelable>::class -> getParcelableArray(key) as T

            Parcelable::class -> getParcelable(key) as? T
            Serializable::class -> getSerializable(key) as? T
            Bundle::class -> getBundle(key) as? T

            else -> null
        }
    }

    // JSON 解码
    val json = getString("__json_$key") ?: return null
    val className = getString("__json_class_$key") ?: return null
    return try {
        val clazz = Class.forName(className)
        bundleGson.fromJson(json, clazz) as? T
    } catch (e: Exception) {
        null
    }
}