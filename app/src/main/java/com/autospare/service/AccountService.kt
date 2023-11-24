package com.autospare.service

import com.autospare.common.google.GoogleUser
import kotlinx.coroutines.flow.Flow

/**
 * Author: Hari K
 * Date: 23/11/2023.
 */
interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<GoogleUser>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
//    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}