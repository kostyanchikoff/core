package com.kostynchikoff.core_application.utils.extensions

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kostynchikoff.core_application.data.constants.CoreConstant.ARG_ANIM
import com.kostynchikoff.core_application.presentation.model.UIActivityAnimation
import com.kostynchikoff.core_application.presentation.ui.activities.CoreActivity
import com.kostynchikoff.core_application.utils.delegates.Theme
import com.kostynchikoff.core_application.utils.permission.ActivityaPermissionImpl
import com.kostynchikoff.core_application.utils.permission.Permission


/**
 * @param activity activity которое нужно открыть
 * @param typeAnimation тип анимации
 * @param arg аргументы
 */
fun <T : Parcelable?> Activity.showActivity(
    activity: Activity,
    typeAnimation: UIActivityAnimation? = null,
    arg: HashMap<String, T>? = hashMapOf()
) {

    startActivity(Intent(this, activity::class.java).apply {
        arg?.forEach {
            putExtra(it.key, it.value)
        }
        putExtra(ARG_ANIM, typeAnimation)
    })

    if (this is CoreActivity) {
        when (typeAnimation) {
            UIActivityAnimation.BOTTOM -> animBottomToTop()
            UIActivityAnimation.RIGHT -> animLeftToRight()
        }
    }

}


/**
 * Открытие фрагмента
 * @param fragment фрагмент который требуеться открыть
 * @param isBackStack фраг который определяет нужет ли стек
 */
fun AppCompatActivity.showFragment(fragment: Fragment, isBackStack: Boolean = true) {
    val fm: FragmentManager = supportFragmentManager
    val tr = fm.beginTransaction()
        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        .show(fragment)
    if (isBackStack) {
        tr.addToBackStack(null)
    }
    tr.commit()
}

/**
 * Запрашиваем опасные разрешения
 * @param permissionList список из разрешений
 * @param request код для который можно отследить в onRequestPermissionsResult
 * @param isPermission callback с флагом, полученино ли зарешение
 */
fun AppCompatActivity.requestPermission(
    permissionList: Array<String>,
    request: Int,
    isPermission: ((Boolean) -> Unit?)? = null
) {
    var permission: Permission? = null

    if (permission == null) {
        permission = ActivityaPermissionImpl(activity = this)
    }

    permission.apply {
        if (isPermission == null) {
            permissionExistFragment(permissionList, request)
            return
        }
        isPermission.invoke(permissionExistFragment(permissionList, request))
    }
}

/**
 * Переход на другое активити и отчистка стека
 * @param activity на которое требуеться переход
 */
fun Activity.showActivityAndClearBackStack(activity: Activity?) {
    if (activity == null) throw Throwable("Укажите activity")
    val intent = Intent(this, activity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    this.finish()
}

/**
 * Переключение темы
 * @param theme в случае Theme.APP стандартная тема приложения в Theme.DARK темная тема приложения
 */
fun Activity.setTheme(theme: Theme) {
    (this as? CoreActivity)?.let {
        setTheme(theme, window)
    }
}


