package org.fastcampus.member.repository

import org.fastcampus.member.entity.MemberAddress

interface MemberAddressRepository {
    fun save(memberAddress: MemberAddress): MemberAddress

    fun findByUserId(userId: Long): List<MemberAddress>
}
