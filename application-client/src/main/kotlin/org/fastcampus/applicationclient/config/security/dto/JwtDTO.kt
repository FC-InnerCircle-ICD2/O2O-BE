package org.fastcampus.applicationclient.config.security.dto

object JwtDTO {
    const val EXPIRATION_TIME: Int = 1000 * 60 * 60 * 24 * 7
    const val TOKEN_PREFIX: String = "Bearer "
    const val HEADER: String = "Authorization"
}
