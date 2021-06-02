package com.squad.tdd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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