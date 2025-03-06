package org.fastcampus.applicationclient.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemberAddressUpdateRequest(
    val id: Long,
    @field:NotBlank(message = "도로명 주소를 입력해 주세요.")
    val roadAddress: String?,
    @field:NotBlank(message = "지번 주소를 입력해 주세요.")
    val jibunAddress: String?,
    val detailAddress: String?,
    val alias: String?,
    @field:NotNull(message = "위도를 입력해 주세요.")
    val latitude: Double?,
    @field:NotNull(message = "경도를 입력해 주세요.")
    val longitude: Double?,
)
