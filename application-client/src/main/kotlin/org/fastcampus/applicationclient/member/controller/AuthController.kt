package org.fastcampus.applicationclient.member.controller

import jakarta.validation.Valid
import org.fastcampus.applicationclient.config.security.dto.response.JwtLoginResponse
import org.fastcampus.applicationclient.member.dto.request.AuthLogoutRequest
import org.fastcampus.applicationclient.member.dto.request.AuthRefreshRequest
import org.fastcampus.applicationclient.member.dto.request.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.response.MemberJoinResponse
import org.fastcampus.applicationclient.member.service.AuthService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    @PostMapping("/join")
    fun join(
        @RequestBody @Valid memberJoinRequestDto: MemberJoinRequest,
    ): APIResponseDTO<MemberJoinResponse> {
        return APIResponseDTO(200, "OK", authService.join(memberJoinRequestDto))
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody @Valid authRefreshRequest: AuthRefreshRequest,
    ): APIResponseDTO<JwtLoginResponse> {
        return APIResponseDTO(200, "OK", authService.refresh(authRefreshRequest))
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody @Valid authLogoutRequest: AuthLogoutRequest,
    ): APIResponseDTO<Unit> {
        return APIResponseDTO(200, "OK", authService.logout(authLogoutRequest))
    }
}
