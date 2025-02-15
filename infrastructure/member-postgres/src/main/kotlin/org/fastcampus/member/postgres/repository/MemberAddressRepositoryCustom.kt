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

    override fun findByUserIdAndMemberAddressType(userId: Long, memberAddressType: MemberAddressType): MemberAddress? {
        return memberAddressJpaRepository.findByUserIdAndMemberAddressType(userId, memberAddressType).map { it.toModel() }.orElse(null)
    }

    override fun countByUserIdAndMemberAddressType(userId: Long, memberAddressType: MemberAddressType): Long {
        return memberAddressJpaRepository.countByUserIdAndMemberAddressType(userId, memberAddressType)
    }

    override fun findByUserId(userId: Long): List<MemberAddress> {
        return memberAddressJpaRepository.findByUserId(userId).filter { !it.isDeleted }.map { it.toModel() }.toList()
    }

    override fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean): MemberAddress? {
        return memberAddressJpaRepository.findByUserIdAndIsDefault(userId, isDefault).map { it.toModel() }.orElse(null)
    }

    override fun findByIdAndUserId(addressId: Long, userId: Long): MemberAddress? {
        return memberAddressJpaRepository.findByIdAndUserId(addressId, userId).map { it.toModel() }.orElse(null)
    }
}
