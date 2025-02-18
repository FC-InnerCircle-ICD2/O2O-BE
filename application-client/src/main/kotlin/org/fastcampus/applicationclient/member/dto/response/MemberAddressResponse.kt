package org.fastcampus.applicationclient.member.dto.response

data class MemberAddressResponse(
    val defaultAddress: MemberAddressDto?,
    val house: MemberAddressDto?,
    val company: MemberAddressDto?,
    val others: List<MemberAddressDto>?,
)

data class MemberAddressDto(
    val id: Long,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
)
