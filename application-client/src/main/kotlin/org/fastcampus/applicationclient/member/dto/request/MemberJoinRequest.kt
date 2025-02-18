package org.fastcampus.applicationclient.member.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * Created by kms0902 on 25. 1. 19..
 */
data class MemberJoinRequest(
    @field:NotBlank(message = "아이디를 입력해 주세요.")
    val signname: String?,
    @field:NotBlank(message = "패스워드를 입력해 주세요.")
    val password: String?,
    @field:NotBlank(message = "성명을 입력해 주세요.")
    val username: String?,
    @field:NotBlank(message = "닉네임을 입력해 주세요.")
    val nickname: String?,
    @field:NotBlank(message = "휴대폰번호를 입력해 주세요.")
    val phone: String?,
    @field:NotNull(message = "주소를 입력해 주세요.")
    @field:Valid
    val address: MemberAddressCreateRequest?,
)
