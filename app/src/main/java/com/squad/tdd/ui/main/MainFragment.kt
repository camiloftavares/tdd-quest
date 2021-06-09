package com.squad.tdd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squad.tdd.AuthApplication
import com.squad.tdd.databinding.MainFragmentBinding
import com.squad.tdd.di.ServiceLocator
import com.squad.tdd.helpers.SignInHelper

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var signInHelper: SignInHelper
    private lateinit var binding: MainFragmentBinding

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory((requireContext().applicationContext as AuthApplication).userPreference)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfoMethod().observe(viewLifecycleOwner) {
            binding.userInfo = it
        }
        signInHelper = ServiceLocator.provideSignInHelper()
        if (!signInHelper.userIsLogged()) {
            findNavController().navigate(MainFragmentDirections.actionRequireSignin())
        }
    }
}