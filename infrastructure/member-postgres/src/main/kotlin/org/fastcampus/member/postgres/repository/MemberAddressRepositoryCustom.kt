package org.fastcampus.member.postgres.repository

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

    override fun findByUserId(userId: Long): List<MemberAddress> {
        return memberAddressJpaRepository.findByUserId(userId).filter { it.isDeleted }.map { it.toModel() }.toList()
    }
}
