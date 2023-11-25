package com.autospare.data


/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
data class Product(
    val name: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val createdTimestamp: Long = 0L,
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", 0L)
}
