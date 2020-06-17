package com.kostynchikoff.core_application.presentation.ui.dialogs

import androidx.fragment.app.DialogFragment
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CoreVariables.LOGIN_ACTIVITY
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.delegates.LiveDataEvent
import com.kostynchikoff.core_application.utils.delegates.LiveDataEventDelegate
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack

// TODO в следущем релизе сделть миграцию все фрагментов на [LiveDataEventDelegate]
abstract class CoreDialogFragment : DialogFragment(), LiveDataEvent by LiveDataEventDelegate(),
    PermissionHandler {

    override fun redirectLogin() {
        activity?.showActivityAndClearBackStack(LOGIN_ACTIVITY)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            when {
                it != CoreConstant.PERMISSION_DENIED -> {
                    confirmPermission()
                    return
                }
                else -> {
                    ignorePermission()
                    return
                }
            }
        }
    }
}