package com.autospare.viewmodel

import com.autospare.data.Order
import com.autospare.service.AccountService
import com.autospare.service.LogService
import com.autospare.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@HiltViewModel
class OrderViewModel @Inject constructor(
    logService: LogService,
    accountService: AccountService,
    private val storageService: StorageService,
) : AutoSpareViewModel(logService, accountService) {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

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

}