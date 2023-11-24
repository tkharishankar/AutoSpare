package com.autospare.viewmodel

import androidx.lifecycle.viewModelScope
import com.autospare.ADD_TASK_SCREEN
import com.autospare.LOGIN_SCREEN
import com.autospare.TASKS_SCREEN
import com.autospare.service.LogService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    logService: LogService,
    private val userPreference: UserPreference,
) : AutoSpareViewModel(logService) {
    fun openAddTask(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(ADD_TASK_SCREEN, TASKS_SCREEN)
        }
    }

    val userId: StateFlow<String?> = userPreference.userName().filter {
        it.isNotEmpty()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

}