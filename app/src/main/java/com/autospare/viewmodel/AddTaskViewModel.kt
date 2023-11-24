package com.autospare.viewmodel

import android.net.Uri
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.data.Product
import com.autospare.service.LogService
import com.autospare.service.StorageService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val userPreference: UserPreference,
) : AutoSpareViewModel(logService) {
    fun addNewProduct(productName: String, productPrice: String, productImagePath: String) {
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
        launchCatching {
            storageService.save(
                Product(
                    name = productName,
                    price = productPrice,
                    imageUrl = productImagePath
                )
            )
        }
    }

}