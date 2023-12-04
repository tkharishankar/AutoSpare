package com.autospare.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.autospare.ADD_PRODUCT_SCREEN
import com.autospare.LOGIN_SCREEN
import com.autospare.common.SnackbarManager
import com.autospare.common.SnackbarMessage
import com.autospare.data.Product
import com.autospare.data.UserData
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
    private val accountService: AccountService,
    private val storageService: StorageService,
) : AutoSpareViewModel(logService, accountService) {
    fun logout(openAndPopUp: (String) -> Unit) {
        launchCatching {
            accountService.logout()
            openAndPopUp(LOGIN_SCREEN)
        }
    }

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

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()


    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getUser() {
        _isLoading.value = true
        viewModelScope.launch {
            accountService.getUserData().collect {
                _user.value = it
                Log.i("tag", "user detail on product page $it")
                _isLoading.value = false
            }
        }
    }
}