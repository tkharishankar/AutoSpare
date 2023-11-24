package com.autospare.service

import com.autospare.data.Product
import kotlinx.coroutines.flow.Flow

/**
 * Author: Hari K
 * Date: 23/11/2023.
 */
interface StorageService {
    val products: Flow<List<Product>>
    suspend fun getProduct(productId: String): Product?
    suspend fun save(product: Product): String
    suspend fun update(product: Product)
    suspend fun delete(productId: String)
}