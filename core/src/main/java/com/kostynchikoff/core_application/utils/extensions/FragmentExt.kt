package com.kostynchikoff.core_application.utils.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kostynchikoff.core_application.utils.permission.Permission
import com.kostynchikoff.core_application.utils.permission.PermissionImpl
import java.lang.NullPointerException

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


fun DialogFragment.show(fm : FragmentManager?) : DialogFragment{
    this.show(fm ?: throw NullPointerException("supportFragmentManager null"), tag)
    return this
}
