package wot.core.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * @author yangsn
 * @date 2025/2/10
 * @des fragment 扩展函数
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int) = supportFragmentManager.inTransaction { add(frameId, fragment) }

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int) = supportFragmentManager.inTransaction { replace(frameId, fragment) }

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String) = supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }

fun Fragment.addChildFragment(fragment: Fragment, frameId: Int) = childFragmentManager.inTransaction { add(frameId, fragment) }

fun Fragment.replaceChildFragment(fragment: Fragment, frameId: Int) = childFragmentManager.inTransaction { replace(frameId, fragment) }

fun Fragment.replaceChildFragment(fragment: Fragment, frameId: Int, tag: String) = childFragmentManager.inTransaction { replace(frameId, fragment, tag) }
