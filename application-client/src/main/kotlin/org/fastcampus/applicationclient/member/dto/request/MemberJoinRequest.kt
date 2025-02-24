package org.fastcampus.applicationclient.member.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * Created by kms0902 on 25. 1. 19..
 */
data class MemberJoinRequest(
    @field:NotBlank(message = "이메일을 입력해주세요.")
    @field:Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
    @field:Email(message = "유효한 이메일 주소를 입력해주세요.")
    val signname: String?,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @field:Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).+$",
        message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.",
    )
    val password: String?,
    @field:NotBlank(message = "이름을 입력해주세요.")
    @field:Size(max = 10, message = "이름은 10자 이내여야 합니다.")
    @field:Pattern(
        regexp = "^[a-zA-Z가-힣]+$",
        message = "이름은 영문, 한글만 가능합니다.",
    )
    val username: String?,
    @field:NotBlank(message = "닉네임을 입력해주세요.")
    @field:Size(max = 10, message = "닉네임은 10자 이내여야 합니다.")
    @field:Pattern(
        regexp = "^[a-zA-Z가-힣0-9]+$",
        message = "닉네임은 영문, 한글, 숫자만 가능합니다.",
    )
    val nickname: String?,
    @field:NotBlank(message = "전화번호를 입력해주세요.")
    @field:Size(min = 10, max = 13, message = "전화번호는 10자 이상 13자 이내여야 합니다.")
    val phone: String?,
    @field:NotNull(message = "주소를 입력해 주세요.")
    @field:Valid
    val address: MemberAddressCreateRequest?,
)
