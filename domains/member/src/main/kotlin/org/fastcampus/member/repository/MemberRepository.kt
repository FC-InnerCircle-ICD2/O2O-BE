package org.fastcampus.member.repository

import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member

/**
 * Created by kms0902 on 25. 1. 19..
 */
interface MemberRepository {
    fun save(member: Member): Member?

    fun findBySignname(signname: String): Member

    fun findByRoleAndSignname(role: Role, signname: String): Member
}
