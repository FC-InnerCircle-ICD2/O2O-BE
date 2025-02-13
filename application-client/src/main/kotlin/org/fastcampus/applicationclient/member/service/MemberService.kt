package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.aop.MemberMetered
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.member.dto.request.MemberAddressCreateRequest
import org.fastcampus.applicationclient.member.dto.request.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.response.MemberAddressCreateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDto
import org.fastcampus.applicationclient.member.dto.response.MemberAddressResponse
import org.fastcampus.applicationclient.member.dto.response.MemberInfoResponse
import org.fastcampus.applicationclient.member.dto.response.MemberJoinResponse
import org.fastcampus.applicationclient.member.exception.MemberException
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.entity.MemberAddress
import org.fastcampus.member.repository.MemberAddressRepository
import org.fastcampus.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Created by kms0902 on 25. 1. 19..
 */
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberAddressRepository: MemberAddressRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @MemberMetered
    fun join(memberJoinRequest: MemberJoinRequest): MemberJoinResponse {
        val role = Role.USER
        val createMember =
            Member(
                null,
                role,
                MemberState.JOIN,
                requireNotNull(memberJoinRequest.signname),
                requireNotNull(passwordEncoder.encode(memberJoinRequest.password)),
                requireNotNull(memberJoinRequest.username),
                requireNotNull(memberJoinRequest.nickname),
                requireNotNull(memberJoinRequest.phone),
            )
        val findMember = memberRepository.findByRoleAndSignname(role, requireNotNull(memberJoinRequest.signname))
        if (findMember != null && MemberState.JOIN == findMember.state) {
            throw MemberException(MemberExceptionResult.DUPLICATE_MEMBER)
        }

        val savedMember = memberRepository.save(createMember)
        return MemberJoinResponse(savedMember?.id)
    }

    @MemberMetered
    fun info(authMember: AuthMember): MemberInfoResponse? {
        val findMember = memberRepository.findById(authMember.id)
        return MemberInfoResponse(findMember.signname, findMember.nickname)
    }

    fun createAddress(memberAddressCreateRequest: MemberAddressCreateRequest, authMember: AuthMember): MemberAddressCreateResponse {
        val addressType = memberAddressCreateRequest.memberAddressType

        val createMemberAddress = MemberAddress(
            null,
            authMember.id,
            requireNotNull(addressType),
            requireNotNull(memberAddressCreateRequest.roadAddress),
            requireNotNull(memberAddressCreateRequest.jibunAddress),
            memberAddressCreateRequest.detailAddress,
            memberAddressCreateRequest.alias,
            requireNotNull(memberAddressCreateRequest.latitude),
            requireNotNull(memberAddressCreateRequest.longitude),
            false,
            null,
        )

        if (MemberAddressType.HOME == addressType || MemberAddressType.COMPANY == addressType) {
            val findMemberAddress = memberAddressRepository.findByUserIdAndMemberAddressType(authMember.id, addressType)
            if (findMemberAddress != null) {
                throw MemberException(MemberExceptionResult.DUPLICATE_MEMBER_ADDRESS_TYPE)
            }
        }

        if (MemberAddressType.OTHERS == addressType) {
            val count = memberAddressRepository.countByUserIdAndMemberAddressType(authMember.id, addressType)
            if (4 < count) {
                throw MemberException(MemberExceptionResult.EXCEEDED_REGISTRABLE_ADDRESS_TYPE)
            }
        }

        val savedMemberAddress = memberAddressRepository.save(createMemberAddress)
        return MemberAddressCreateResponse(requireNotNull(savedMemberAddress.id))
    }

    @MemberMetered
    fun findAddress(authMember: AuthMember): MemberAddressResponse? {
        val findMemberAddress = memberAddressRepository.findByUserId(authMember.id)

        var defaultAddress: MemberAddressDto? = null
        var latestUpdatedAt: LocalDateTime? = null

        val houseList = mutableListOf<MemberAddressDto>()
        val companyList = mutableListOf<MemberAddressDto>()
        val othersList = mutableListOf<MemberAddressDto>()

        for (address in findMemberAddress) {
            val addressDto = MemberAddressDto(
                id = address.id!!,
                roadAddress = address.roadAddress,
                jibunAddress = address.jibunAddress,
                detailAddress = address.detailAddress ?: "",
                latitude = address.latitude,
                longitude = address.longitude,
            )

            // defaultAddress
            if (latestUpdatedAt == null || (address.updatedAt != null && requireNotNull(address.updatedAt) > latestUpdatedAt)) {
                latestUpdatedAt = address.updatedAt
                defaultAddress = addressDto
            }

            when (address.memberAddressType) {
                MemberAddressType.HOME -> houseList.add(addressDto)
                MemberAddressType.COMPANY -> companyList.add(addressDto)
                else -> othersList.add(addressDto)
            }
        }

        return MemberAddressResponse(
            defaultAddress,
            houseList.firstOrNull(),
            companyList.firstOrNull(),
            othersList,
        )
    }
}
