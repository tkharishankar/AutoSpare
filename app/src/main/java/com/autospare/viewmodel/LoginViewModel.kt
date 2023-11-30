package com.autospare.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.autospare.LOGIN_SCREEN
import com.autospare.PRODUCT_SCREEN
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.common.google.GoogleUser
import com.autospare.service.AccountService
import com.autospare.service.LogService
import com.autospare.service.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
) : AutoSpareViewModel(logService,accountService) {
    fun onSignInClick(user: GoogleUser?, openAndPopUp: (String, String) -> Unit) {
        Log.i("Tag", "user: ${user.toString()}")
        viewModelScope.launch {
            if (user == null) {
                return@launch
            }
            val name = user.fullName ?: user.givenName ?: user.familyName
            if (name.isNullOrEmpty()) {
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Username not found"))
                return@launch
            }
            val mailId = user.email
            if (mailId.isNullOrEmpty()) {
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Mail id not found"))
                return@launch
            }
            when (val result = accountService.authenticate(name, mailId)) {
                is LoginState.Failure -> {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(result.message))
                }

                LoginState.Success -> {
                    launchCatching {
                        openAndPopUp(PRODUCT_SCREEN, LOGIN_SCREEN)
                    }
                }
            }
        }
    }
}
