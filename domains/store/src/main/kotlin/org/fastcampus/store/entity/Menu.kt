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
@Table(name = "TB_MENU")
class Menu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,

    @Column(name = "NAME")
    val name: String?,

    @Column(name = "PRICE")
    val price: String?,

    @Column(name = "DESC")
    val desc: String?,

    @Column(name = "IMG_URL")
    val imgUrl: String?,

    @Column(name = "IS_SOLDED_OUT")
    val isSoldOut: String?,

    @Column(name = "IS_HIDED")
    val isHided: String?,

    @Column(name = "STORE_ID")
    val storeId: String?,

    @Column(name = "Field")
    val field: String?
)
