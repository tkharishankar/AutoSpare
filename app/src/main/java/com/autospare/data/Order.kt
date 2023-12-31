package com.autospare.data

import kotlinx.serialization.Serializable

/**
 * Author: Senthil
 * Date: 29/11/2023.
 */
@Serializable
data class Order(
    val userId: String = "",
    val username: String = "",
    val orderId: String = "",
    val products: List<String> = emptyList(),
    val createdTimestamp: Long = 0L,
    var status: Status = Status.NONE,
)


enum class Status {
    CREATED,

    CONFIRMED,

    INPROGRESS,

    SHIPPED,

    DELIVERED,

    NONE
}