package com.autospare.common.google

import android.util.Log
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
data class GoogleUser(
    val sub: String? = "",
    val email: String? = "",
    val emailVerified: Boolean? = false,
    val fullName: String? = "",
    val givenName: String? = "",
    val familyName: String? = "",
    val picture: String? = "",
    val issuedAt: Long? = 0L,
    val expirationTime: Long? = 0L,
    val locale: String? = "",
    val isAnonymous: Boolean = false,
)

fun getUserFromTokenId(tokenId: String): GoogleUser? {
    try {
        val jwt = JWT(tokenId)
        return GoogleUser(
            sub = jwt.claims[Claims.SUB]?.asString(),
            email = jwt.claims[Claims.EMAIL]?.asString(),
            emailVerified = jwt.claims[Claims.EMAIL_VERIFIED]?.asBoolean(),
            fullName = jwt.claims[Claims.FUll_NAME]?.asString(),
            givenName = jwt.claims[Claims.GIVEN_NAME]?.asString(),
            familyName = jwt.claims[Claims.FAMILY_NAME]?.asString(),
            picture = jwt.claims[Claims.PICTURE]?.asString(),
            issuedAt = jwt.claims[Claims.ISSUED_AT]?.asLong(),
            expirationTime = jwt.claims[Claims.EXPIRATION_TIME]?.asLong(),
            locale = jwt.claims[Claims.LOCALE]?.asString()
        )
    } catch (e: Exception) {
        Log.e("SignInCompose", e.toString())
        return null
    } catch (e: DecodeException) {
        Log.e("SignInCompose", e.toString())
        return null
    }
}