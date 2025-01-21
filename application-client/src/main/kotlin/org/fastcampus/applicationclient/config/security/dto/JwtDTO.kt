package org.fastcampus.applicationclient.config.security.dto

object JwtDTO {
//    const val ACCESS_TOKEN_EXPIRATION_TIME: Int = 1000 * 60 * 15 // 15분
    const val ACCESS_TOKEN_EXPIRATION_TIME: Int = 1000 * 60 * 1 // 1분

//    const val REFRESH_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 60 * 24 * 7 // 7일
    const val REFRESH_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 2 // 2분
    const val TOKEN_PREFIX: String = "Bearer "
    const val HEADER: String = "Authorization"
}
