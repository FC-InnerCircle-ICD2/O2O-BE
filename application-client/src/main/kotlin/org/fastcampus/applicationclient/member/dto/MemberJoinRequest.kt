package org.fastcampus.applicationclient.member.dto

import jakarta.validation.constraints.NotBlank

/**
 * Created by kms0902 on 25. 1. 19..
 */
data class MemberJoinRequest(
    @field:NotBlank(message = "이메일을 입력해 주세요.")
    val signname: String?, // 정상
    @field:NotBlank(message = "패스워드를 입력해 주세요.")
    val password: String?, // 정상
    @field:NotBlank(message = "성명을 입력해 주세요.")
    val username: String?, // 정상
    @field:NotBlank(message = "닉네임을 입력해 주세요.")
    val nickname: String?, // 정상
    @field:NotBlank(message = "휴대폰번호를 입력해 주세요.")
    val phone: String?, // 정상
)
