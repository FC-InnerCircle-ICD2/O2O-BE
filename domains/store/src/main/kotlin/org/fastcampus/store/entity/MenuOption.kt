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
@Table(name = "TB_MENU_OPTION")
class MenuOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,

    @Column(name = "NAME")
    val name: String?,

    @Column(name = "PRICE")
    val price: String?,

    @Column(name = "MENU_OPTION_ID")
    val menuOptionId: String?,

    @Column(name = "IS_SOLD_OUT")
    val isSoldOut: String?,

    @Column(name = "ORDER")
    val order: String?
)
