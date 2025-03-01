package org.fastcampus.applicationclient.member.dto.response

data class MemberInfoResponse(
    val signname: String,
    val nickname: String,
    val address: DefaultAddress?,
) {
    data class DefaultAddress(
        val roadAddress: String,
        val jibunAddress: String,
        val detailAddress: String,
        val latitude: Double,
        val longitude: Double,
    )
}
