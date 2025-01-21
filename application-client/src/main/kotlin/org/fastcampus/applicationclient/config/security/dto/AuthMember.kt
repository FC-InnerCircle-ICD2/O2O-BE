package org.fastcampus.applicationclient.config.security.dto

import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role

data class AuthMember(
    val id: Long,
    val role: Role,
    val state: MemberState,
)
