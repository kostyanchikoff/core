package com.kostynchikoff.core_application.presentation.controllers

import android.os.CountDownTimer
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.data.prefs.SourcesLocalDataSource
import com.kostynchikoff.core_application.presentation.ui.activities.CoreActivity
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.ref.WeakReference

/**
 * Используеться для отслеживания активности пользователя
 * Для чего используеться
 * 1. Если пользователь бездействует во время открытого приложения и в тоже время авторизован
 * то по истечению определенного времени перекидываем пользователя на экран авторизации
 * 2. Если пользователь свирнул приложение и прошло после окрытия больше заданного времени
 * то после открытия перекидывыаем на экран логина
 */
interface TrackUseApplication {
    /**
     * Задаем время локальной сессии в минутах
     */
    fun setMinuteSession(minute: Long)

    /**
     * Даем понять контроллеру что пользователь использует приложение
     */
    fun onTouchEvent()

    /**
     * Даем понять контроллеру что пользователь начал пользоваться приложением
     */
    fun onStartTrack(activity: CoreActivity)

    /**
     * Даем понять контроллеру что пользователь вернулся в приложение
     */
    fun onResumeTrack()

    /**
     * Даем понять контролеру что пользователь свернул приложение
     */
    fun onPauseTrack()

    /**
     * Разрушаем activity для избежания утечки памяти
     */
    fun onDestroyTrack()
}

/**
 * @param isUseLocalSession нужно ли использовать локальную сессию
 */
class TrackUseApplicationController(private val isUseLocalSession: Boolean = true) :
    TrackUseApplication,
    KoinComponent {

    private val sourceDataPref by inject<SourcesLocalDataSource>()
    private val secureDataPref by inject<SecurityDataSource>()

    private var mActivity: WeakReference<CoreActivity>? = null
    private var countDownTimer: CountDownTimer? = null

    private var minuteSession = 3L
    private val timeSession = minuteSession * 60 * 1000

    override fun onStartTrack(activity: CoreActivity) {
        mActivity = WeakReference(activity)
        startTimer()
    }

    private fun createCountDownTimer(): CountDownTimer {
        return object : CountDownTimer(timeSession, 1000) {
            override fun onFinish() {
                redirectLoginInCaseOfInaction()
            }

            override fun onTick(p0: Long) {
                // do nothing
            }
        }
    }

    override fun setMinuteSession(minute: Long) {
        minuteSession = minute
    }

    override fun onTouchEvent() {
        sourceDataPref.setLastTimeUseApplication(System.currentTimeMillis())
        stopTimer()
        startTimer()
    }

    private fun startTimer() {
        if (isUseLocalSession)
            countDownTimer = createCountDownTimer().start()
    }

    private fun stopTimer() {
        if (isUseLocalSession) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }

    override fun onDestroyTrack() {
        mActivity?.clear()
        mActivity = null

        if (isUseLocalSession) {
            sourceDataPref.removeLastTimeUseApplication()
        }
    }

    override fun onResumeTrack() {
        if (isUseLocalSession) {
            if ((System.currentTimeMillis() - timeSession) >= sourceDataPref.getLastTimeUseApplication() &&
                sourceDataPref.getLastTimeUseApplication() != 0L
            ) {
                redirectLoginInCaseOfInaction()
                return
            }
        }

        stopTimer()
        startTimer()
    }

    override fun onPauseTrack() {
        stopTimer()
    }

    private fun redirectLoginInCaseOfInaction() {
        mActivity?.get()?.redirectLogin()
    }
}