package org.fastcampus.applicationadmin.config.security.code

enum class TokenType(val tokenName: String) {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
}
