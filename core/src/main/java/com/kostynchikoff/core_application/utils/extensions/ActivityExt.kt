package com.kostynchikoff.core_application.utils.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
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

/**
 * Находит фрагменты для определенного NavHostFragment-a(Контейнера фрагментов)
 * @param fragmentManager FragmentManager
 * @param navHostFragmentId Id navHostFragment-а от которого вы хотите получить фрагменты
 */
fun Activity.getFragmentsFromNavHostById(
    fragmentManager: FragmentManager,
    navHostFragmentId: Int
): List<Fragment>? {
    val navHostFragment = fragmentManager.findFragmentById(navHostFragmentId)
    return navHostFragment?.childFragmentManager?.fragments
}

/**
 * Анимация для активити слайд снизу вверх
 */
fun Activity.animBottomToTop() {
    if (this is CoreActivity) {
        animBottomToTop()
    }
}

/**
 * Анимация для активити слайд сверху вниз
 */
fun Activity.animTopToBottom() {
    if (this is CoreActivity) {
        animTopToBottom()
    }
}

/**
 * Анимация для активити слайд слева вправо
 */
fun Activity.animLeftToRight() {
    if (this is CoreActivity) {
        animLeftToRight()
    }
}

/**
 * Анимация для активити слайд справа налево
 */
fun Activity.rightToLeft() {
    if (this is CoreActivity) {
        rightToLeft()
    }
}

/**
 * Сделать статус бар прозрачным
 */
fun Activity.setupStatusBar() {
    window?.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

/**
 * Открывает Google Play App по packageName
 */
fun Activity.openGooglePlayByPackageName(packageName: String) {
    val googlePlayIntent = try {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")
        )
    } catch (anfe: ActivityNotFoundException) {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
    }
    startActivity(googlePlayIntent)
}

/**
 * Проверяет установлено ли приложении на устройстве по packageName
 */
fun Activity.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}


/**
 * Открытие Activity с другого модуля
 * @param path полный путь к пакету где разположено Activity
 */
fun Activity.showModuleActivity(path: String) {
    var intent: Intent? = null
    try {
        intent = Intent(
            this,
            Class.forName(path)
        )
        startActivity(intent)
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }
}

/**
 * Проверка какую тему использует устройство
 * Theme.DARK - темная тема
 * Theme.APP - светлая тема
 */
fun Activity.getSystemUIMode(): Theme {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> Theme.DARK
        Configuration.UI_MODE_NIGHT_NO -> Theme.APP
        else -> Theme.APP
    }
}

