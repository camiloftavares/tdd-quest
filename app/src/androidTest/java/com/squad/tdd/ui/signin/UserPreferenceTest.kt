package com.squad.tdd.ui.signin

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squad.tdd.ui.signin.preferences.UserPreferenceImp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserPreferenceTest {
    private val dataStore = UserPreferenceImp(ApplicationProvider.getApplicationContext())

    @ExperimentalCoroutinesApi
    @Test
    fun shouldReturnNullWhenGetUserInfoDoesntExistInDataStore() = runBlockingTest {
        dataStore.getUserInfo().collect {
            Assert.assertNull(it)
        }
    }
}