package org.fastcampus.member.entity

import org.fastcampus.member.code.MemberAddressType
import java.time.LocalDateTime

/**
 * Created by kms0902 on 25. 2. 11..
 */
class MemberAddress(
    var id: Long? = null,
    var userId: Long,
    val memberAddressType: MemberAddressType,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String?,
    val alias: String?,
    val latitude: Double,
    val longitude: Double,
    val isDeleted: Boolean,
    val updatedAt: LocalDateTime?,
)
