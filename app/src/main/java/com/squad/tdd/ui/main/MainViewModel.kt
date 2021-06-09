package com.squad.tdd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference

class MainViewModel(private val userPreference: UserPreference) : ViewModel() {

    val userInfo: LiveData<UserInfo> get() {
        return userPreference.getUserInfo().asLiveData()
    }

    fun getUserInfoMethod(): LiveData<UserInfo> {
        return userPreference.getUserInfo().asLiveData()
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(userPreference) as T
    }
}