package com.autospare.data

import android.net.Uri

/**
 * Author: Hari K
 * Date: 23/11/2023.
 */
data class Product(
    val name: String,
    val price: String,
    val imageUrl: String,
    val createdTimestamp: Long = 0L,
)
