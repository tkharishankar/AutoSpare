package com.autospare.service

import kotlinx.coroutines.flow.Flow

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
interface UserPreference {

    fun userName(): Flow<String>

    suspend fun saveUserName(name: String)
}