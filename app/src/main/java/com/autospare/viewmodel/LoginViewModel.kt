package com.autospare.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.autospare.LOGIN_SCREEN
import com.autospare.TASKS_SCREEN
import com.autospare.common.google.GoogleUser
import com.autospare.service.LogService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    logService: LogService,
    private val userPreference: UserPreference
) : AutoSpareViewModel(logService) {

    fun onSignInClick(user: GoogleUser?, openAndPopUp: (String, String) -> Unit) {
        Log.i("Tag", "user: ${user.toString()}")

        viewModelScope.launch {
            if (user != null) {
                user.email?.let { userPreference.saveUserName(it) }
            }
        }
        launchCatching {
            openAndPopUp(TASKS_SCREEN, LOGIN_SCREEN)
        }
    }
}
