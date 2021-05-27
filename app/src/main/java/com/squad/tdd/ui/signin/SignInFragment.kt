package com.squad.tdd.ui.signin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squad.tdd.R
import com.squad.tdd.helpers.PermissionHelper
import com.squad.tdd.helpers.PermissionHelperImpl
import kotlinx.android.synthetic.main.sign_in_fragment.view.*

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModels()

    lateinit var permissionHelper: PermissionHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        permissionHelper = PermissionHelperImpl(activity as Activity)
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.sign_in_btn.setOnClickListener { onSignInButtonClick() }
    }

    private fun onSignInButtonClick() {
        if (!permissionHelper.isLocationPermissionGranted()) {
            permissionHelper.requestLocationPermission()
        }
    }
}