package org.fastcampus.member.postgres.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity
import org.fastcampus.member.code.MemberAddressType
import org.fastcampus.member.entity.MemberAddress
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * Created by kms0902 on 25. 2. 11..
 */
@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "MEMBER_ADDRESS")
class MemberAddressJpaEntity(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "USER_ID")
    val userId: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRESS_TYPE", nullable = false)
    val memberAddressType: MemberAddressType,
    @Column(name = "ROAD_ADDRESS", nullable = false)
    val roadAddress: String = "",
    @Column(name = "JIBUN_ADDRESS", nullable = false)
    val jibunAddress: String = "",
    @Column(name = "DETAIL_ADDRESS")
    val detailAddress: String = "",
    @Column(name = "LATITUDE")
    val latitude: Double,
    @Column(name = "LONGITUDE")
    val longitude: Double,
    @Column(name = "ALIAS")
    val alias: String = "",
    @Column(name = "IS_DELETED")
    val isDeleted: Boolean,
) : BaseEntity()

fun MemberAddress.toJpaEntity() =
    MemberAddressJpaEntity(
        id,
        userId,
        memberAddressType,
        roadAddress,
        jibunAddress,
        detailAddress,
        latitude,
        longitude,
        alias,
        isDeleted,
    )

fun MemberAddressJpaEntity.toModel() =
    MemberAddress(
        id,
        userId,
        memberAddressType,
        roadAddress,
        jibunAddress,
        detailAddress,
        latitude,
        longitude,
        alias,
        isDeleted,
    )
