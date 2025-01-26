package org.fastcampus.applicationadmin.config.security.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role

data class AuthMember
    @JsonCreator
    constructor(
        @field:NotBlank(message = "고유번호를 입력해 주세요.")
        @JsonProperty("id")
        val id: Long,
        @field:NotBlank(message = "권한을 입력해 주세요.")
        @JsonProperty("role")
        val role: Role,
        @field:NotBlank(message = "상태를 입력해 주세요.")
        @JsonProperty("state")
        val state: MemberState,
    )
