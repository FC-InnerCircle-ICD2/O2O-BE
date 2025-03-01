package org.fastcampus.member.postgres.repository

import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.entity.MemberAddress
import org.fastcampus.member.postgres.entity.toJpaEntity
import org.fastcampus.member.postgres.entity.toModel
import org.fastcampus.member.repository.MemberAddressRepository
import org.springframework.stereotype.Repository

@Repository
class MemberAddressRepositoryCustom(
    private val memberAddressJpaRepository: MemberAddressJpaRepository,
) : MemberAddressRepository {
    override fun save(memberAddress: MemberAddress): MemberAddress {
        return memberAddressJpaRepository.save(memberAddress.toJpaEntity()).toModel()
    }

    override fun countByUserIdAndMemberAddressTypeAndIsDeleted(
        userId: Long,
        memberAddressType: MemberAddressType,
        isDeleted: Boolean,
    ): Long {
        return memberAddressJpaRepository.countByUserIdAndMemberAddressTypeAndIsDeleted(userId, memberAddressType, isDeleted)
    }

    override fun findByUserId(userId: Long): List<MemberAddress> {
        return memberAddressJpaRepository.findByUserId(userId).filter { !it.isDeleted }.map { it.toModel() }.sortedByDescending { it.id }
    }

    override fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean): MemberAddress? {
        return memberAddressJpaRepository.findByUserIdAndIsDefault(userId, isDefault).map { it.toModel() }.orElse(null)
    }

    override fun findByIdAndUserId(addressId: Long, userId: Long): MemberAddress? {
        return memberAddressJpaRepository.findByIdAndUserId(addressId, userId).map { it.toModel() }.orElse(null)
    }
}
