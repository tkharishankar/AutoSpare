package com.autospare.viewmodel

import androidx.lifecycle.viewModelScope
import com.autospare.ADD_PRODUCT_SCREEN
import com.autospare.PRODUCT_SCREEN
import com.autospare.service.LogService
import com.autospare.service.StorageService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
    logService: LogService,
    private val userPreference: UserPreference,
    private val storageService: StorageService,
    ) : AutoSpareViewModel(logService) {
    fun openAddProduct(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(ADD_PRODUCT_SCREEN, PRODUCT_SCREEN)
        }
    }

    val products = storageService.products

    val userId: StateFlow<String?> = userPreference.userName().filter {
        it.isNotEmpty()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

}