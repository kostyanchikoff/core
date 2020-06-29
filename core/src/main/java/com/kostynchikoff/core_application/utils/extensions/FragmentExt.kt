package com.kostynchikoff.core_application.utils.extensions

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kostynchikoff.core_application.utils.permission.Permission
import com.kostynchikoff.core_application.utils.permission.PermissionImpl


/**
 * Запрос тяжелый разрешений
 * @param permissionList спсок из разрешений
 * @param request код при перехвате
 * @param isPermission лямбда возращаем было ли разрешение получено ранее
 */
fun Fragment.requestPermission(
    permissionList: Array<String>,
    request: Int,
    isPermission: ((Boolean) -> Unit?)? = null
) {
    var permission: Permission? = null

    if (permission == null) {
        permission = PermissionImpl(fragment = this)
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
 * Отпрытие диалогой без передачи fragment менеджера
 */
fun DialogFragment.show(fm : FragmentManager?) : DialogFragment{
    this.show(fm ?: throw NullPointerException("supportFragmentManager null"), tag)
    return this
}


/**
 * Перехватчик перехода назад для fragment-ов в navigation library для Activity
 */
fun FragmentActivity.doOnBackPresed(block : () -> Unit){
    val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                block()
            }
        }
    onBackPressedDispatcher.addCallback(this, callback)
}



/**
 * Находим все открытие нижние диалоги во [FragmentManager]
 * @param manager FragmentManager
 * @param nameDialog имя диалога (достаем имя через рефлексию) BottomSheetDialogFragment::class.simpleName
 * @return найденный диалог в противном случае null
 */
fun Fragment.getDialogBottomSheetDialogFragmentByName(
    nameDialog: String?
): BottomSheetDialogFragment? {
    val fragments = fragmentManager?.fragments?.toMutableList()
    return fragments?.filter { it is BottomSheetDialogFragment }
        ?.find { it::class.simpleName == nameDialog } as? BottomSheetDialogFragment
}


/**
 * Находит открытие [BottomSheetDialogFragment]
 * @param dialogs список [BottomSheetDialogFragment]
 * @return [BottomSheetDialogFragment] первый найденный диалог
 */
fun Fragment.searchBottomSheerDialogFragment(
    vararg dialogs : String?
): BottomSheetDialogFragment? {
    var dialog: BottomSheetDialogFragment? = null
    dialogs.forEach {
        dialog = getDialogBottomSheetDialogFragmentByName(it)
        if (dialog != null) {
            return dialog
        }
    }

    return dialog
}


/**
 * Перехватчик перехода назад для fragment-ов в navigation library для Fragment
 */
fun Fragment.onBackPressed(block: () -> Unit) {
    val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                block()
            }
        }
    requireActivity().onBackPressedDispatcher.addCallback(this, callback)
}

