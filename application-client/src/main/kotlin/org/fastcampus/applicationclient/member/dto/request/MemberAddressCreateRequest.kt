package org.fastcampus.applicationclient.member.dto.request

import jakarta.validation.constraints.NotBlank
import org.fastcampus.member.code.MemberAddressType

data class MemberAddressCreateRequest(
    @field:NotBlank(message = "주소 타입을 입력해 주세요.")
    val addressType: MemberAddressType?,
    @field:NotBlank(message = "도로명 주소를 입력해 주세요.")
    val roadAddress: String?,
    @field:NotBlank(message = "지번 주소를 입력해 주세요.")
    val jibunAddress: String?,
    val detailAddress: String?,
    val alias: String?,
    @field:NotBlank(message = "위도를 입력해 주세요.")
    val latitude: Double?,
    @field:NotBlank(message = "경도를 입력해 주세요.")
    val longitude: Double?,
)
