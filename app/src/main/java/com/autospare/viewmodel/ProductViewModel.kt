package com.autospare.viewmodel

import androidx.lifecycle.viewModelScope
import com.autospare.ADD_PRODUCT_SCREEN
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.data.Product
import com.autospare.service.AccountService
import com.autospare.service.LogService
import com.autospare.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
    logService: LogService,
    accountService: AccountService,
    private val storageService: StorageService,
) : AutoSpareViewModel(logService, accountService) {
    fun openAddProduct(popUp: (String) -> Unit) {
        launchCatching {
            popUp(ADD_PRODUCT_SCREEN)
        }
    }

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun toggleProductSelection(product: Product) {
        val updatedProducts = _products.value.toMutableList()
        val index = updatedProducts.indexOf(product)
        if (index != -1) {
            val updatedProduct =
                updatedProducts[index].copy(isSelected = !updatedProducts[index].isSelected)
            updatedProducts[index] = updatedProduct
            _products.value = updatedProducts
        }
    }

    suspend fun getProducts() {
        storageService.products.collectLatest {
            _products.value = it
        }
    }

    fun createOrder() {
        viewModelScope.launch {
            user.value?.let { userData ->
                storageService.createOrder(
                    userData.email,
                    userData.name,
                    products.value.filter { it.isSelected })
            }
            SnackbarManager.showMessage(SnackbarMessage.StringSnackbar("Order created successfully"))
            val updatedProducts = products.value.map { product ->
                if (product.isSelected) {
                    product.copy(isSelected = false)
                } else {
                    product
                }
            }
            _products.value = updatedProducts
        }
    }
}