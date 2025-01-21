package org.fastcampus.applicationclient.member.controller

import jakarta.validation.Valid
import org.fastcampus.applicationclient.member.dto.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.MemberJoinResponse
import org.fastcampus.applicationclient.member.service.MemberService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.web.bind.annotation.PostMapping
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
    @PostMapping("/join")
    fun join(
        @RequestBody @Valid memberJoinRequestDto: MemberJoinRequest,
    ): APIResponseDTO<MemberJoinResponse> {
        return APIResponseDTO(200, "OK", memberService.join(memberJoinRequestDto))
    }
}
