package org.fastcampus.member.entity

import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role

/**
 * Created by kms0902 on 25. 1. 19..
 */
data class Member(
    val id: Long? = null,
    val role: Role,
    val state: MemberState,
    val signname: String,
    val password: String,
    val username: String,
    val nickname: String,
    val phone: String,
)
