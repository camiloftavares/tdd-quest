package com.squad.tdd.ui.signin.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squad.tdd.data.UserInfo
import com.squad.tdd.preferences.UserPreference
import com.squad.tdd.utils.zip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceImp(private val context: Context) : UserPreference {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preference")
        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
        private val AVATAR = stringPreferencesKey("avatar")
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        saveStringDataStore(TOKEN, userInfo.idToken)
        saveStringDataStore(NAME, userInfo.name)
        saveStringDataStore(EMAIL, userInfo.email)
        saveStringDataStore(AVATAR, userInfo.avatar)
    }

    override fun getUserInfo(): Flow<UserInfo> {
        return getToken.zip(getName, getEmail, getAvatar) { a, b, c, d ->
            UserInfo(a, b, c, d)
        }
    }

    private suspend fun saveStringDataStore(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    private val getToken: Flow<String>
        get() = context.dataStore.data.map {
            it[TOKEN] ?: throw Exception("Invalid Token ID.")
        }

    private val getName: Flow<String>
        get() = context.dataStore.data.map {
            it[NAME] ?: ""
        }

    private val getEmail: Flow<String>
        get() = context.dataStore.data.map {
            it[EMAIL] ?: ""
        }

    private val getAvatar: Flow<String>
        get() = context.dataStore.data.map {
            it[AVATAR] ?: ""
        }
}