package com.squad.tdd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.helpers.SignInHelperImpl

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var signInHelper: SignInHelper

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInHelper = SignInHelperImpl()
        if (!signInHelper.userIsLogged()) {
            findNavController().navigate(MainFragmentDirections.actionRequireSignin())
        }
    }
}