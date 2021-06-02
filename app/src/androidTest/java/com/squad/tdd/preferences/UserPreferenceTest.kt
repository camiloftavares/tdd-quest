package com.squad.tdd.preferences

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.data.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.any
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserPreferenceTest {
    private val dataStore = UserPreferenceImp(ApplicationProvider.getApplicationContext())

    @Test
    fun shouldReturnUserInfoWhenGetUserInfoTokenIsValid() = runBlocking {
        dataStore.saveUserInfo(UserInfo("id_token"))
        val data = dataStore.getUserInfo().first()
        Assert.assertThat(data, `is`(any(UserInfo::class.java)))
    }

    @Test
    fun shouldReturnExceptionWhenGetUserInfoTokenIsNull(): Unit = runBlocking {
        try {
            dataStore.getUserInfo().first()
        } catch (e: Exception) {
            Assert.assertThat(e.message, `is`("Invalid Token ID."))
        }
    }

    @Test
    fun shouldReturnUserInfoWhenGetUserInfoIsTriggered() = runBlocking {
        val dummyUserInfo = UserInfo("id_token", "name", "email", "avatar")

        dataStore.saveUserInfo(dummyUserInfo)
        val data = dataStore.getUserInfo().first()
        Assert.assertThat(data, `is`(dummyUserInfo))
    }

}