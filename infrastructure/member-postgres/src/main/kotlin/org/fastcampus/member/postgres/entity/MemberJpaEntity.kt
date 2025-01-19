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
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * Created by kms0902 on 25. 1. 19..
 */
@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "MEMBERS")
class MemberJpaEntity(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    val role: Role,
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    val state: MemberState,
    @Column(name = "SIGN_NAME", nullable = false)
    val signname: String = "",
    @Column(name = "PASSWORD", nullable = false)
    val password: String = "",
    @Column(name = "USER_NAME", nullable = false)
    val username: String = "",
    @Column(name = "NICK_NAME", nullable = false)
    val nickname: String = "",
    @Column(name = "PHONE", nullable = false)
    val phone: String = "",
) : BaseEntity()

fun Member.toJpaEntity() =
    MemberJpaEntity(
        id,
        role,
        state,
        signname,
        password,
        username,
        nickname,
        phone,
    )

fun MemberJpaEntity.toModel() =
    Member(
        id,
        role,
        state,
        signname,
        password,
        username,
        nickname,
        phone,
    )
