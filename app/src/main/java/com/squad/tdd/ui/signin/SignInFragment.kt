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
import androidx.navigation.fragment.findNavController
import com.squad.tdd.AuthApplication
import com.squad.tdd.MainActivity
import com.squad.tdd.R
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.di.requirePermissionManager
import com.squad.tdd.helpers.PermissionManager
import kotlinx.android.synthetic.main.sign_in_fragment.view.*


class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(ServiceLocator.provideGoogleVerifyUseCase((requireContext().applicationContext as AuthApplication)))
    }

    lateinit var permissionManager: PermissionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionManager = requireActivity().requirePermissionManager()
        view.sign_in_btn.setOnClickListener { onSignInButtonClick() }
    }

    private fun onSignInButtonClick() {
        permissionManager.handlePermission(Manifest.permission.ACCESS_FINE_LOCATION,
                rationaleTextId = R.string.permission_rationale_default_text,
                onPermissionGranted = {
                    findNavController().navigate(SignInFragmentDirections.actionMainScreen())
                },
                onPermissionDenied = {
                }
        )
    }
}