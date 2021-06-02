package com.squad.tdd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squad.tdd.R
import com.squad.tdd.helpers.SignInHelper
import com.squad.tdd.helpers.SignInHelperImpl
import com.squad.tdd.preferences.UserPreferenceImp
import com.squad.tdd.utils.ViewModelFactory

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var signInHelper: SignInHelper
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = UserPreferenceImp(requireContext())
        viewModelFactory = ViewModelFactory(userPreference)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getUserInfoMethod().observe(viewLifecycleOwner) {

        }

        signInHelper = SignInHelperImpl()
        if (!signInHelper.userIsLogged()) {
            findNavController().navigate(MainFragmentDirections.actionRequireSignin())
        }
    }
}