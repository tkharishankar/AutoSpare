package com.autospare.data

/**
 * Author: Hari K
 * Date: 29/11/2023.
 */
data class Order(
    val orderId: String = "",
    val products: List<String> = emptyList(),
    val createdTimestamp: Long = 0L,
    val status: Status = Status.NONE,
) {
    // Add a no-argument constructor
    constructor() : this("", emptyList(), 0L, Status.NONE)
}


enum class Status {
    CREATED,

    CONFIRMED,

    INPROGRESS,

    SHIPPED,

    DELIVERED,

    NONE
}