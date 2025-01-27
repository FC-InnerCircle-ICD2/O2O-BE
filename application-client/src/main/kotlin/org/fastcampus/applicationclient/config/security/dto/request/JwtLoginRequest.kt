package org.fastcampus.applicationclient.config.security.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

/**
 * Created by kms0902 on 25. 1. 20..
 */
data class JwtLoginRequest
    @JsonCreator
    constructor(
        @field:NotBlank(message = "아이디를 입력해 주세요")
        @JsonProperty("signname")
        val signname: String?,
        @field:NotBlank(message = "비밀번호를 입력해 주세요")
        @JsonProperty("password")
        val password: String?,
    )
