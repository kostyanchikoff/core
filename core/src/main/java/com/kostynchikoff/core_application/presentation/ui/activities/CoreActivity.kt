package com.kostynchikoff.core_application.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.delegates.DarkTheme
import com.kostynchikoff.core_application.utils.delegates.DarkThemeDelegate
import com.kostynchikoff.core_application.utils.delegates.TransitionAnimation
import com.kostynchikoff.core_application.utils.delegates.TransitionAnimationActivityDelegate
import com.kostynchikoff.core_application.utils.extensions.getSystemUIMode
import com.kostynchikoff.core_application.utils.extensions.toast
import com.kostynchikoff.core_application.utils.wrappers.EventObserver


abstract class CoreActivity(lay: Int) : AppCompatActivity(lay), ResultLiveDataHandler,
    DarkTheme by DarkThemeDelegate(),
    TransitionAnimation by TransitionAnimationActivityDelegate(), PermissionHandler {

    protected val errorMessageObserver = EventObserver<String> { toast(it) }

    protected val errorMessageByTypeObserver = EventObserver<UIValidation> {
        errorByType(type = it.type, msg = it.message)
    }

    open fun redirectLogin() {
        // реализовать в случае базовой функциональности
    }

    /**
     * Для того чтобы отслеживать статусы необходимо подписаться в Activity
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


    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme(window, getSystemUIMode())
        initTransition(this)
        super.onCreate(savedInstanceState)
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


