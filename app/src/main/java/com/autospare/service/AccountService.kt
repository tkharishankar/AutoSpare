package com.autospare.service

import com.autospare.data.UserData
import com.autospare.service.state.LoginState
import kotlinx.coroutines.flow.Flow

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
interface AccountService {
    suspend fun authenticate(name: String, mailId: String): LoginState
    fun getUserData(): Flow<UserData?>
    abstract suspend fun logout(): Boolean
}