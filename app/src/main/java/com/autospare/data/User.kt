package com.autospare.data

import kotlinx.serialization.Serializable

/**
 * Author: Senthil
 * Date: 30/11/2023.
 */
@Serializable
data class User(
    var name: String = "",
    var email: String = "",
)
