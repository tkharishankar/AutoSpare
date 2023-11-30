package com.autospare.data

import kotlinx.serialization.Serializable


/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
@Serializable
data class Product(
    val productId: String = "",
    val name: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val createdTimestamp: Long = 0L,
    val isSelected: Boolean = false,
)
