package org.fastcampus.applicationadmin.member.controller

import jakarta.validation.Valid
import org.fastcampus.applicationadmin.config.security.dto.response.JwtLoginResponse
import org.fastcampus.applicationadmin.member.dto.request.AuthRefreshRequest
import org.fastcampus.applicationadmin.member.service.AuthService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by kms0902 on 25. 1. 26..
 */
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/refresh")
    fun refresh(
        @RequestBody @Valid authRefreshRequest: AuthRefreshRequest,
    ): APIResponseDTO<JwtLoginResponse> {
        return APIResponseDTO(200, "OK", authService.refresh(authRefreshRequest))
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") accessToken: String,
        @RequestHeader("Refresh-Token") refreshToken: String,
    ): APIResponseDTO<String> {
        return APIResponseDTO(200, "OK", authService.logout(accessToken, refreshToken))
    }
}
