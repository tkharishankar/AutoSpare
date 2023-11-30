package com.autospare.service

import com.autospare.data.UserData
import kotlinx.coroutines.flow.Flow

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
interface UserPreference {

    fun getUserData(): Flow<UserData?>


    suspend fun saveUserData(user: UserData)
}