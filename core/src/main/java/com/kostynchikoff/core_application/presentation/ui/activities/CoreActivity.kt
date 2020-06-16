package com.kostynchikoff.core_application.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.data.constants.CoreVariables.ID_FRAGMENT
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.callback.OnBackPressedHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.delegates.DarkTheme
import com.kostynchikoff.core_application.utils.delegates.DarkThemeDelegate
import com.kostynchikoff.core_application.utils.delegates.TransitionAnimation
import com.kostynchikoff.core_application.utils.delegates.TransitionAnimationActivityDelegate
import com.kostynchikoff.core_application.utils.extensions.toast
import com.kostynchikoff.core_application.utils.wrappers.EventObserver


abstract class CoreActivity(lay: Int) : AppCompatActivity(lay), ResultLiveDataHandler,
    DarkTheme by DarkThemeDelegate(),
    TransitionAnimation by TransitionAnimationActivityDelegate() {

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
        initTheme(window)
        initTransition(this)
        super.onCreate(savedInstanceState)
    }

    private val errorMessageObserver = EventObserver<String> { toast(it) }

    protected  val errorMessageByTypeObserver = EventObserver<UIValidation>{
        errorByType(it.message, it.type)
    }

    open fun redirectLogin() {
        // реализовать в случае базовой функциональности
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(ID_FRAGMENT)
        (fragment as? OnBackPressedHandler)?.let {
            it.backButtonClickEvent()
            return
        }
        super.onBackPressed()
    }
}