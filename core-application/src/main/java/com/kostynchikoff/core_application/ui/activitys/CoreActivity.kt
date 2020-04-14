package com.kostynchikoff.core_application.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.data.constants.CoreVariables.ID_FRAGMENT
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.utils.callback.OnBackPresedHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.event.EventObserver
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack
import com.kostynchikoff.core_application.utils.extensions.toast

abstract class CoreActivity(lay: Int) : AppCompatActivity(lay), ResultLiveDataHandler {

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

    private val errorObserver = EventObserver<String> { toast(it) }

    private fun redirectLogin() = showActivityAndClearBackStack(CoreVariables.LOGIN_ACTIVITY)

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(ID_FRAGMENT)
        (fragment as? OnBackPresedHandler)?.let {
            it.backButtonClickEvent()
            return
        }
        super.onBackPressed()
    }
}