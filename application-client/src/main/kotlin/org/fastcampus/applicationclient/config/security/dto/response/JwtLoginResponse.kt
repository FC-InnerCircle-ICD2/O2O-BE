package org.fastcampus.applicationclient.config.security.dto.response

data class JwtLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: String,
    val refreshTokenExpiresIn: String,
)
