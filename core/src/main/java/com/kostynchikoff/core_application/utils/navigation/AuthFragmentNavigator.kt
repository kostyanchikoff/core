package com.kostynchikoff.core_application.utils.navigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.utils.extensions.showModuleActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

@Navigator.Name("coreFragment")
class AuthFragmentNavigator(
    private val activity: FragmentActivity,
    manager: FragmentManager,
    containerId: Int,
    private val pathAuthActivity : String
) : FragmentNavigator(activity.baseContext, manager, containerId), KoinComponent {

    companion object {
        var destinationPending: Destination? = null
        var argsPending: Bundle? = null
        var navOptionsPending: NavOptions? = null
        var navigatorExtrasPending: Navigator.Extras? = null
    }

    private val authToken by inject<SecurityDataSource>()

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val authDestination = destination as? AuthDestination
        val isAuthFragment = authDestination?.isAuthFragment() ?: false

        if (authToken.getAccessToken().isNullOrEmpty() && isAuthFragment) {
            activity.showModuleActivity(pathAuthActivity)
            addPendingData(destination, args, navOptions, navigatorExtras)
            return null
        }

        return super.navigate(destination, args, navOptions, navigatorExtras)
    }


    override fun createDestination(): Destination = AuthDestination(this)


    private fun addPendingData(
        destination: Destination?,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        destinationPending = destination
        argsPending = args
        navOptionsPending = navOptions
        navigatorExtrasPending = navigatorExtras
    }

    fun clearPendingData() {
        destinationPending = null
        argsPending = null
        navOptionsPending = null
        navigatorExtrasPending = null
    }

    @NavDestination.ClassType(Fragment::class)
    open class AuthDestination(fragmentNavigator: Navigator<out Destination>) :
        FragmentNavigator.Destination(fragmentNavigator) {


        private var isAuthFragment = false

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            val a = context.resources.obtainAttributes(
                attrs,
                R.styleable.AuthFragmentNavigator
            )
            val className = a.getString(R.styleable.AuthFragmentNavigator_android_name)
            isAuthFragment = a.getBoolean(R.styleable.AuthFragmentNavigator_authFragment, false)
            className?.let { setClassName(it) }
            a.recycle()
        }


        fun isAuthFragment() = isAuthFragment
    }
}
