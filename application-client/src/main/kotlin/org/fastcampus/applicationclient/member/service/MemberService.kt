package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.aop.MemberMetered
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.member.dto.request.MemberAddressCreateRequest
import org.fastcampus.applicationclient.member.dto.request.MemberAddressUpdateRequest
import org.fastcampus.applicationclient.member.dto.response.MemberAddressCreateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDefaultUpdateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDeleteResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressDto
import org.fastcampus.applicationclient.member.dto.response.MemberAddressResponse
import org.fastcampus.applicationclient.member.dto.response.MemberAddressUpdateResponse
import org.fastcampus.applicationclient.member.dto.response.MemberInfoResponse
import org.fastcampus.applicationclient.member.exception.MemberException
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.entity.MemberAddress
import org.fastcampus.member.repository.MemberAddressRepository
import org.fastcampus.member.repository.MemberRepository
import org.springframework.stereotype.Service

/**
 * Created by kms0902 on 25. 1. 19..
 */
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberAddressRepository: MemberAddressRepository,
) {
    @MemberMetered
    fun info(authMember: AuthMember): MemberInfoResponse? {
        val findMember = memberRepository.findById(authMember.id)
        return MemberInfoResponse(findMember.signname, findMember.nickname)
    }

    fun createAddress(memberAddressCreateRequest: MemberAddressCreateRequest, authMember: AuthMember): MemberAddressCreateResponse {
        deleteDefaultAddressByMemberId(authMember.id)

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
            isDefault = true,
            isDeleted = false,
            updatedAt = null,
        )

        if (MemberAddressType.HOME == addressType || MemberAddressType.COMPANY == addressType) {
            val findMemberAddress = memberAddressRepository.findByUserIdAndMemberAddressType(authMember.id, addressType)
            if (findMemberAddress != null) {
                throw MemberException(MemberExceptionResult.DUPLICATE_MEMBER_ADDRESS_TYPE)
            }
        }

        if (MemberAddressType.OTHER == addressType) {
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
        var house: MemberAddressDto? = null
        var company: MemberAddressDto? = null
        val othersList = mutableListOf<MemberAddressDto>()

        for (address in findMemberAddress) {
            val addressDto = MemberAddressDto(
                id = address.id!!,
                roadAddress = address.roadAddress,
                jibunAddress = address.jibunAddress,
                detailAddress = address.detailAddress ?: "",
                latitude = address.latitude,
                longitude = address.longitude,
                isDefault = address.isDefault,
            )

            if (address.isDefault) {
                defaultAddress = addressDto
            }

            when (address.memberAddressType) {
                MemberAddressType.HOME -> house = addressDto
                MemberAddressType.COMPANY -> company = addressDto
                else -> othersList.add(addressDto)
            }
        }

        return MemberAddressResponse(
            defaultAddress,
            house,
            company,
            othersList,
        )
    }

    @MemberMetered
    fun updateAddress(
        addressId: Long,
        memberAddressUpdateRequest: MemberAddressUpdateRequest,
        authMember: AuthMember,
    ): MemberAddressUpdateResponse {
        val findMemberAddress = memberAddressRepository.findByIdAndUserId(addressId, authMember.id)
            ?: throw MemberException(MemberExceptionResult.NOT_FOUND_ADDRESS)

        val updatedAddress = findMemberAddress.copy(
            roadAddress = memberAddressUpdateRequest.roadAddress,
            jibunAddress = memberAddressUpdateRequest.jibunAddress,
            detailAddress = memberAddressUpdateRequest.detailAddress,
            alias = memberAddressUpdateRequest.alias,
            latitude = memberAddressUpdateRequest.latitude,
            longitude = memberAddressUpdateRequest.longitude,
        )

        memberAddressRepository.save(updatedAddress)

        return MemberAddressUpdateResponse(addressId)
    }

    @MemberMetered
    fun updateDefaultAddress(addressId: Long, authMember: AuthMember): MemberAddressDefaultUpdateResponse {
        val memberId = authMember.id
        deleteDefaultAddressByMemberId(memberId)

        val findMemberAddress = memberAddressRepository.findByIdAndUserId(addressId, memberId)
            ?: throw MemberException(MemberExceptionResult.NOT_FOUND_ADDRESS)
        findMemberAddress.updateIsDefault(true)
        memberAddressRepository.save(findMemberAddress)

        return MemberAddressDefaultUpdateResponse(requireNotNull(findMemberAddress.id))
    }

    @MemberMetered
    fun deleteAddress(addressId: Long, authMember: AuthMember): MemberAddressDeleteResponse {
        val findMemberAddress = memberAddressRepository.findByIdAndUserId(addressId, authMember.id)
            ?: throw MemberException(MemberExceptionResult.NOT_FOUND_ADDRESS)

        if (findMemberAddress.isDefault) {
            throw MemberException(MemberExceptionResult.NOT_ALLOWED_DELETION_DEFAULT_ADDRESS)
        }

        findMemberAddress.delete()
        memberAddressRepository.save(findMemberAddress)

        return MemberAddressDeleteResponse(findMemberAddress.id!!)
    }

    private fun deleteDefaultAddressByMemberId(memberId: Long) {
        val findMemberAddress = memberAddressRepository.findByUserIdAndIsDefault(memberId, true)
        if (findMemberAddress != null) {
            findMemberAddress.updateIsDefault(false)
            memberAddressRepository.save(findMemberAddress)
        }
    }
}
