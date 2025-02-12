package org.fastcampus.applicationclient.fixture

import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.member.code.Role

fun createAuthMember(id: Long = 1L, role: Role = Role.USER): AuthMember {
    return AuthMember(
        id = id,
        role = role,
    )
}
