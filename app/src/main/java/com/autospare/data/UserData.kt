package com.autospare.data

import kotlinx.serialization.Serializable

/**
 * Author: Hari K
 * Date: 30/11/2023.
 */
@Serializable
data class UserData(
    var name: String = "",
    var email: String = "",
    val isAdmin: Boolean = false,
)
