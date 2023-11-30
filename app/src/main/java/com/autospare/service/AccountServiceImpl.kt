package com.autospare.service

import com.autospare.data.User
import com.autospare.data.UserData
import com.autospare.service.state.CreateUserState
import com.autospare.service.state.GetUserState
import com.autospare.service.state.LoginState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */

class AccountServiceImpl @Inject constructor(
    private val storageService: StorageService,
    private val userPreference: UserPreference,
) : AccountService {
    override suspend fun authenticate(name: String, mail: String): LoginState {
        println("authenticate")
        return when (storageService.getUserDetail(mail)) {
            is GetUserState.OnUser -> {
                saveUserData(User(name = name, email = mail))
                LoginState.Success
            }

            GetUserState.UserNotFound -> {
                saveUser(User(name = name, email = mail))
            }
        }
    }

    private suspend fun saveUserData(user: User) {
        val state = storageService.checkUserIsAdmin(user.email)
        userPreference.saveUserData(UserData(name = user.name, email = user.email, isAdmin = state))
    }

    override fun getUserData(): Flow<UserData?> {
        return userPreference.getUserData()
    }

    private suspend fun saveUser(user: User): LoginState {
        return when (val state = storageService.saveUser(user)) {
            is CreateUserState.CreateError -> {
                LoginState.Failure(state.message)
            }

            CreateUserState.Created -> {
                saveUserData(user)
                LoginState.Success
            }
        }
    }
}
