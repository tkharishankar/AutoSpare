package com.autospare.service

import com.autospare.data.Order
import com.autospare.data.Product
import com.autospare.data.Status
import com.autospare.data.User
import com.autospare.service.state.CreateUserState
import com.autospare.service.state.GetUserState
import kotlinx.coroutines.flow.Flow

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
interface StorageService {
    val products: Flow<List<Product>>
    suspend fun getProduct(productId: String): Product?
    suspend fun save(product: Product): String
    suspend fun update(product: Product)
    suspend fun delete(productId: String)
    suspend fun createOrder(userMailId: String, username: String, products: List<Product>): String
    suspend fun getUserDetail(mail: String): GetUserState
    suspend fun saveUser(user: User): CreateUserState
    suspend fun checkUserIsAdmin(email: String): Boolean
    fun ordersByUser(email: String?):  Flow<List<Order>>
    fun orders():  Flow<List<Order>>
    suspend fun getOrder(orderId: String): Flow<Order?>
    suspend fun productByIds(productIds: List<String>):  Flow<List<Product>>
    suspend fun setOrderStatus(orderId: String?, status: Status): Flow<Order?>
}