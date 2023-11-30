package com.autospare.service

import com.autospare.data.UserData
import com.autospare.module.Preferences
import com.autospare.service.KEYS.KEY_USER_ID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
class UserPreferenceImpl @Inject constructor(
    private val preferences: Preferences,
) : UserPreference {
    override fun getUserData(): Flow<UserData?> = preferences.get(KEY_USER_ID)

    override suspend fun saveUserData(user: UserData) {
        preferences.set(KEY_USER_ID,user)
    }
}

object KEYS {
    val KEY_USER_ID = "user_id"
}
