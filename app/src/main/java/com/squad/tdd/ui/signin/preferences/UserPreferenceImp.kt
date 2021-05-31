package com.squad.tdd.ui.signin.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceImp(val context: Context) : UserPreference {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "name")

    companion object {
        val NAME = stringPreferencesKey("name")
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(): Flow<String> {
        return context.dataStore.data.map {
            it[NAME] ?: ""
        }
    }


}