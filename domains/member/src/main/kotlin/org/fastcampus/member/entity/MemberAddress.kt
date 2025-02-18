package org.fastcampus.member.entity

import org.fastcampus.member.code.MemberAddressType
import java.time.LocalDateTime

/**
 * Created by kms0902 on 25. 2. 11..
 */
data class MemberAddress(
    var id: Long? = null,
    var userId: Long,
    val memberAddressType: MemberAddressType,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String?,
    val alias: String?,
    val latitude: Double,
    val longitude: Double,
    var isDefault: Boolean,
    var isDeleted: Boolean,
    val updatedAt: LocalDateTime?,
) {
    fun updateIsDefault(isDefault: Boolean) {
        this.isDefault = isDefault
    }

    fun delete() {
        this.isDeleted = true
    }
}
