package org.fastcampus.applicationadmin.fixture

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member

fun createAuthMember(id: Long = 1L, role: Role = Role.USER): AuthMember {
    return AuthMember(
        id = id,
        role = role,
    )
}

fun createMember(
    id: Long? = 1L,
    role: Role = Role.USER,
    state: MemberState? = MemberState.JOIN,
    signname: String = "test_signname",
    password: String = "securepassword",
    username: String = "test_user",
    nickname: String = "test_nickname",
    phone: String = "010-1234-5678",
): Member {
    return Member(
        id = id,
        role = role,
        state = state,
        signname = signname,
        password = password,
        username = username,
        nickname = nickname,
        phone = phone,
    )
}
