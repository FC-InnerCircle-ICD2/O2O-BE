package org.fastcampus.applicationclient.member.dto.request

import jakarta.validation.constraints.NotBlank

data class AuthRefreshRequest(
    @field:NotBlank(message = "액세스 토큰을 입력해 주세요.")
    val accessToken: String,
    @field:NotBlank(message = "리프레쉬 토큰을 입력해 주세요.")
    val refreshToken: String,
)
