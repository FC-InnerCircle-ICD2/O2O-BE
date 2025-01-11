package org.fastcampus.store.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Created by brinst07 on 25. 1. 11..
 */
@Entity
@Table(name = "TB_MENU_OPTION_GROUP")
data class MenuOptionGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,

    @Column(name = "NAME")
    val name: String?,

    @Column(name = "MENU_ID")
    val menuId: String?,

    @Column(name = "MIN_SEL")
    val minSel: String?,

    @Column(name = "MAX_SEL")
    val maxSel: String?,

    @Column(name = "ORDER")
    val order: String?
)
