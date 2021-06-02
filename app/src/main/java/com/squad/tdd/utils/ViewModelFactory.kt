package com.squad.tdd.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.ui.main.MainViewModel

class ViewModelFactory(private val userPreference: UserPreference): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(userPreference) as T
    }
}