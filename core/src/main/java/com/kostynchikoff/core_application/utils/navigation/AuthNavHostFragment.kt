package com.kostynchikoff.core_application.utils.navigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.navigation.fragment.NavHostFragment
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.data.constants.CoreConstant
import org.koin.core.KoinComponent


class AuthNavHostFragment : NavHostFragment(), KoinComponent {

    private var pathAuthActivity = CoreConstant.EMPTY

    companion object {
        var isUserSuccessAuthorization = false
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        val navHost = context.obtainStyledAttributes(
            attrs,
            R.styleable.AuthNavHostFragment
        )
        pathAuthActivity =
            navHost.getString(R.styleable.AuthNavHostFragment_pathAuthActivity).orEmpty()

        navHost.recycle()
    }

    override fun createFragmentNavigator() =
        AuthFragmentNavigator(requireActivity(), childFragmentManager, id, pathAuthActivity)
}