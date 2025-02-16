package org.fastcampus.member.repository

import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.entity.MemberAddress

interface MemberAddressRepository {
    fun save(memberAddress: MemberAddress): MemberAddress

    fun findByUserIdAndMemberAddressType(userId: Long, memberAddressType: MemberAddressType): MemberAddress?

    fun countByUserIdAndMemberAddressType(userId: Long, memberAddressType: MemberAddressType): Long

    fun findByUserId(userId: Long): List<MemberAddress>

    fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean): MemberAddress?

    fun findByIdAndUserId(addressId: Long, userId: Long): MemberAddress?
}
