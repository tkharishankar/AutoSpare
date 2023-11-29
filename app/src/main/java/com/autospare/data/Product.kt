package com.autospare.data


/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
data class Product(
    val productId: String = "",
    val name: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val createdTimestamp: Long = 0L,
    val isSelected: Boolean = false,
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", 0L, false)
}
