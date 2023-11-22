package com.autospare.service

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}