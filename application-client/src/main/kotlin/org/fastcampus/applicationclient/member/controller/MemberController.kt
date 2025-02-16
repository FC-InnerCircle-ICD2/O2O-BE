package org.fastcampus.applicationclient.member.controller

import jakarta.validation.Valid
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.member.dto.request.MemberAddressCreateRequest
import org.fastcampus.applicationclient.member.dto.request.MemberAddressUpdateRequest
import org.fastcampus.applicationclient.member.dto.response.MemberAddressCreateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDefaultUpdateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDeleteResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressUpdateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberInfoResponse
import org.fastcampus.applicationclient.member.service.MemberService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by kms0902 on 25. 1. 19
 */
@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {
    @JwtAuthenticated
    @GetMapping
    fun info(
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<MemberInfoResponse> {
        return APIResponseDTO(200, "OK", memberService.info(authMember))
    }

    @JwtAuthenticated
    @PostMapping("/address")
    fun createAddress(
        @AuthenticationPrincipal authMember: AuthMember,
        @RequestBody @Valid address: MemberAddressCreateRequest,
    ): APIResponseDTO<MemberAddressCreateResponse> {
        return APIResponseDTO(200, "OK", memberService.createAddress(address, authMember))
    }

    @JwtAuthenticated
    @GetMapping("/address")
    fun findAddress(
        @AuthenticationPrincipal authMember: AuthMember,
    ): APIResponseDTO<MemberAddressResponse> {
        return APIResponseDTO(200, "OK", memberService.findAddress(authMember))
    }

    @JwtAuthenticated
    @PutMapping("/address/{id}")
    fun updateAddress(
        @AuthenticationPrincipal authMember: AuthMember,
        @PathVariable("id") addressId: Long,
        @RequestBody @Valid memberAddressUpdateRequest: MemberAddressUpdateRequest,
    ): APIResponseDTO<MemberAddressUpdateResponse> {
        return APIResponseDTO(200, "OK", memberService.updateAddress(addressId, memberAddressUpdateRequest, authMember))
    }

    @JwtAuthenticated
    @PutMapping("/address/{id}/default")
    fun updateDefaultAddress(
        @AuthenticationPrincipal authMember: AuthMember,
        @PathVariable("id") addressId: Long,
    ): APIResponseDTO<MemberAddressDefaultUpdateResponse> {
        return APIResponseDTO(200, "OK", memberService.updateDefaultAddress(addressId, authMember))
    }

    @JwtAuthenticated
    @DeleteMapping("/address/{id}")
    fun deleteAddress(
        @AuthenticationPrincipal authMember: AuthMember,
        @PathVariable("id") addressId: Long,
    ): APIResponseDTO<MemberAddressDeleteResponse> {
        return APIResponseDTO(200, "OK", memberService.deleteAddress(addressId, authMember))
    }
}
