package com.kostynchikoff.core_application.presentation.ui.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.constants.CoreConstant.PERMISSION_DENIED
import com.kostynchikoff.core_application.data.constants.CoreVariables.LOGIN_ACTIVITY
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.extensions.goPendingFragment
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack
import com.kostynchikoff.core_application.utils.wrappers.EventObserver

/**
 * Фрагмент для авторизованой зоны, в случаем ели не пребуеться сразу переходить
 * в авторизовый фрагмент передаем  [isGoToPendingFragment] false
 */
abstract class CoreFragment(id: Int, private val isGoToPendingFragment : Boolean = true) : Fragment(id), ResultLiveDataHandler, PermissionHandler {

    /**
     * Для того чтобы отслеживать статусы необходимо подписаться во Fragment-е
     */
    protected val statusObserver = Observer<Status> {
        it?.let {
            when (it) {
                Status.SHOW_LOADING -> showLoader()
                Status.HIDE_LOADING -> hideLoader()
                Status.REDIRECT_LOGIN -> redirectLogin()
                Status.SUCCESS -> success()
                else -> return@let
            }
        }
    }

    protected val errorMessageObserver = EventObserver<String> {
        error(it)
    }

    /**
     * Подписка на ошибки
     * Возврашает строку и тип ошибки, удобно когда нужно вывести ошибку для конкретного случая
     */
    protected  val errorMessageByTypeObserver = EventObserver<UIValidation>{
        errorByType(type = it.type, msg = it.message)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            when {
                it != PERMISSION_DENIED -> {
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

    override fun onResume() {
        super.onResume()
        if (isGoToPendingFragment)
            goPendingFragment()
    }


    private fun redirectLogin() = activity?.showActivityAndClearBackStack(LOGIN_ACTIVITY)
}