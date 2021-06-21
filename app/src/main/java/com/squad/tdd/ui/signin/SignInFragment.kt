package com.squad.tdd.ui.signin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squad.tdd.AuthApplication
import com.squad.tdd.BuildConfig
import com.squad.tdd.R
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.di.requirePermissionManager
import com.squad.tdd.helpers.PermissionManager
import com.squad.tdd.helpers.SignInHelper
import kotlinx.android.synthetic.main.sign_in_fragment.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(ServiceLocator.provideGoogleVerifyUseCase((requireContext().applicationContext as AuthApplication)))
    }

    lateinit var permissionManager: PermissionManager
    lateinit var signInHelper: SignInHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionManager = requireActivity().requirePermissionManager()
        signInHelper = ServiceLocator.provideSignInHelper(requireActivity())

        lifecycleScope.launch {
            signInHelper.signInResult().collect { userInfo ->
                viewModel.verifyGoogleCoroutine(userInfo).observe(
                    this@SignInFragment.viewLifecycleOwner,
                    {
                        if (it.isApiSuccess) {
                            findNavController().navigate(SignInFragmentDirections.actionMainScreen())
                        }
                    })
            }
        }

        view.sign_in_btn.setOnClickListener { onSignInButtonClick() }
    }

    private fun onSignInButtonClick() {
        permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
            rationaleTextId = R.string.permission_rationale_default_text,
            onPermissionGranted = {
                signInHelper.showSignInIntent()
            },
            onPermissionDenied = {
            }
        )
    }
}