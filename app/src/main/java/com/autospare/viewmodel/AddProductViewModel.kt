package com.autospare.viewmodel

import androidx.lifecycle.viewModelScope
import com.autospare.PRODUCT_SCREEN
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.data.Product
import com.autospare.service.LogService
import com.autospare.service.StorageService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@HiltViewModel
class AddProductViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val userPreference: UserPreference,
) : AutoSpareViewModel(logService) {
    fun addNewProduct(
        productName: String,
        productPrice: String,
        productImagePath: String,
        popUp: (String) -> Unit,
    ) {
        if (productName.isEmpty()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Product name should not be blank"))
            return
        }

        if (productPrice.isEmpty()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Price should not be blank"))
            return
        }

        if (productImagePath.isEmpty()) {
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Please add product image!"))
            return
        }
        println(productImagePath)
        viewModelScope.launch {
            storageService.save(
                Product(
                    name = productName,
                    price = productPrice,
                    imageUrl = productImagePath
                )
            )
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Product added successfully"))
            launchCatching {
                popUp(PRODUCT_SCREEN)
            }
        }
    }

}