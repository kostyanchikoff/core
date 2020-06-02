package com.kostynchikoff.core_application.presentation.ui.activities

import android.os.Bundle
import android.view.MotionEvent
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.presentation.controllers.TrackUseApplication
import com.kostynchikoff.core_application.presentation.controllers.TrackUseApplicationController
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack

/**
 * Использовать в авторизованой зоне
 * @param lay layout
 * @param isUseLocalSession если нужно использовать локальную сессию
 */
abstract class CoreAuthorizedActivity(lay: Int, isUseLocalSession: Boolean = true) :
    CoreActivity(lay),
    TrackUseApplication by TrackUseApplicationController(isUseLocalSession) {

    override fun redirectLogin() = showActivityAndClearBackStack(CoreVariables.LOGIN_ACTIVITY)

    override fun onCreate(savedInstanceState: Bundle?) {
        onStartTrack(this)
        super.onCreate(savedInstanceState)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        onTouchEvent()
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        onResumeTrack()
    }

    override fun onPause() {
        super.onPause()
        onPauseTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyTrack()
    }
}