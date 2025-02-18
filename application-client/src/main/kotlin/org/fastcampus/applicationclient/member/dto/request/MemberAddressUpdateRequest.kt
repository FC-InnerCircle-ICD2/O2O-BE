package org.fastcampus.applicationclient.member.dto.request

data class MemberAddressUpdateRequest(
    val id: Long,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String?,
    val alias: String?,
    val latitude: Double,
    val longitude: Double,
)
