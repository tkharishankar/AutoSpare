package com.autospare.service

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}