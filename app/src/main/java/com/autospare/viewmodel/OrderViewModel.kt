package com.autospare.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.autospare.ORDER_ID
import com.autospare.data.Order
import com.autospare.data.Product
import com.autospare.data.Status
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
class OrderViewModel @Inject constructor(
    logService: LogService,
    savedStateHandle: SavedStateHandle,
    private val accountService: AccountService,
    private val storageService: StorageService,
) : AutoSpareViewModel(logService, accountService) {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()

    init {
        val orderId = savedStateHandle.get<String>(ORDER_ID)
        if (orderId != null) {
            launchCatching {
                storageService.getOrder(orderId).collectLatest {
                    _order.value = it
                    if (it != null) {
                        getProducts(it.products)
                    }
                }
            }
        }
    }

    suspend fun getOrders() {
        if (user.value?.isAdmin == true) {
            storageService.orders().collectLatest {
                _orders.value = it
            }
        } else {
            storageService.ordersByUser(user.value?.email).collectLatest {
                _orders.value = it
            }
        }
    }

    private fun getProducts(productIds: List<String>) {
        launchCatching {
            storageService.productByIds(productIds).collectLatest {
                _products.value = it
            }
        }
    }

    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getUser() {
        _isLoading.value = true
        viewModelScope.launch {
            accountService.getUserData().collect {
                _user.value = it
            }
        }
    }

    fun updateOrderStatus(orderId: String?, status: Status) {
        launchCatching {
            storageService.setOrderStatus(orderId,status).collectLatest {
                _order.value = it
            }
        }
    }

}