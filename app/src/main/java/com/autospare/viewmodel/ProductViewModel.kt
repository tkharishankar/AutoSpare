package com.autospare.viewmodel

import androidx.lifecycle.viewModelScope
import com.autospare.ADD_PRODUCT_SCREEN
import com.autospare.PRODUCT_SCREEN
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.data.Product
import com.autospare.service.LogService
import com.autospare.service.StorageService
import com.autospare.service.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
            storageService.createOrder(products.value.filter { it.isSelected })
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

    val userId: StateFlow<String?> = userPreference.userName().filter {
        it.isNotEmpty()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )
}