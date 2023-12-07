package com.autospare.service.state

import com.autospare.data.User

/**
 * Author: Senthil
 * Date: 30/11/2023.
 */
sealed class GetUserState {
    data class OnUser(val user: User?) : GetUserState()
    data object UserNotFound : GetUserState()
}

sealed class CreateUserState {
    data object Created : CreateUserState()
    data class CreateError(val message: String) : CreateUserState()
}

sealed class LoginState {
    data object Success : LoginState()
    data class Failure(val message: String) : LoginState()

}

