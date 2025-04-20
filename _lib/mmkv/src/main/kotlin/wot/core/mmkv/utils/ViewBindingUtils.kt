package wot.core.mmkv.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import wot.core.logcat.Logcat
import wot.core.logcat.TagRegistry
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * @author yangsn
 * @date 2025/2/10
 * @des ViewBinding 创建工具类
 */
object ViewBindingUtils {

    const val TAG = "ViewBindingUtils"

    init {
        TagRegistry.register(TAG)
    }

    fun <VB : ViewBinding?> create(clazz: Class<*>?, inflater: LayoutInflater?, root: ViewGroup?, attachToRoot: Boolean = false): VB? {
        val bindingClass = getBindingClass(clazz)
        var binding: VB? = null
        if (bindingClass != null) {
            try {
                val method = bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
                binding = method.invoke(null, inflater, root, attachToRoot) as VB
            } catch (e: InvocationTargetException) {
                Logcat.e(TAG, "create err! maybe layoutXml inflater: ", e)
            } catch (e: Exception) {
                Logcat.e(TAG, "create err: ", e)
            }
        }
        Logcat.run { d(TAG, "create fail") }
        return binding
    }

    private fun getBindingClass(clazz: Class<*>?): Class<*>? {
        if (clazz == null) {
            return null
        }
        // 父类
        var superclass = clazz.superclass
        // 泛型父类
        var genericSuperclass = clazz.genericSuperclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                val types = genericSuperclass.actualTypeArguments
                if (types == null || types.size == 0) {
                    return null
                }
                var bindingClass: Class<*>? = null
                for (type in types) {
                    if (type is Class<*>) {
                        val temp = type
                        if (ViewBinding::class.java.isAssignableFrom(temp)) {
                            bindingClass = temp
                            // FIXME: 2021/11/29 暂时用于Fragment 泛型参数只有one
                        }
                    }
                }
                if (bindingClass != null) {
                    return bindingClass
                }
            }
            // 找不到当前的类的泛型参数, 继续找上一个父类
            genericSuperclass = superclass.genericSuperclass
            // 父类
            superclass = clazz.superclass
        }
        Logcat.run { d(TAG, "getBindingClass fail clazz: " + clazz.canonicalName) }
        return null
    }
}
