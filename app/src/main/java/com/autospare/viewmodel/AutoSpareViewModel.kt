package com.autospare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage.Companion.toSnackbarMessage
import com.autospare.data.UserData
import com.autospare.service.AccountService
import com.autospare.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
open class AutoSpareViewModel(private val logService: LogService, private val accountService: AccountService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()
    fun getUser() {
        viewModelScope.launch {
            accountService.getUserData().collect {
                _user.value = it
            }
        }
    }
}
