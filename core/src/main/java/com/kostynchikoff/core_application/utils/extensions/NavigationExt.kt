package com.kostynchikoff.core_application.utils.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kostynchikoff.core_application.utils.navigation.AuthFragmentNavigator
import com.kostynchikoff.core_application.utils.navigation.AuthFragmentNavigator.Companion.argsPending
import com.kostynchikoff.core_application.utils.navigation.AuthFragmentNavigator.Companion.destinationPending
import com.kostynchikoff.core_application.utils.navigation.AuthFragmentNavigator.Companion.navOptionsPending
import com.kostynchikoff.core_application.utils.navigation.AuthFragmentNavigator.Companion.navigatorExtrasPending
import com.kostynchikoff.core_application.utils.navigation.AuthNavHostFragment.Companion.isUserSuccessAuthorization

/**
 * При успешной авторизации уведомляем навигаю что пользователь авторизован
 */
fun Fragment.successAuth() {
    isUserSuccessAuthorization = true
    activity?.finish()
}

/**
 * Функция дает возможность автомитически прости к тому фрагмента где потребовалась авторизация
 */
fun Fragment.goPendingFragment() {
    if (isUserSuccessAuthorization) {
        val navigator =
            findNavController().navigatorProvider.getNavigator(AuthFragmentNavigator::class.java)
        val notEmptyDestination = destinationPending ?: return

        findNavController().navigate(
            notEmptyDestination.id,
            argsPending,
            navOptionsPending,
            navigatorExtrasPending
        )
        navigator.clearPendingData()
    }

}
