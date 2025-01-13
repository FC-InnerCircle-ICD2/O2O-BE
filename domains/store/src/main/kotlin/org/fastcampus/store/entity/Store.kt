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
@Table(name = "TB_STORE")
class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "NAME")
    val name: String?,
    @Column(name = "ADDRESS")
    val address: String?,
    @Column(name = "LATITUDE")
    val latitude: String?,
    @Column(name = "LONGITUDE")
    val longitude: String?,
    @Column(name = "BORDER")
    val border: String?,
    @Column(name = "OWNER_ID")
    val ownerId: String?,
    @Column(name = "TEL")
    val tel: String?,
    @Column(name = "IMAGE_THUMBNAIL")
    val imageThumbnail: String?,
    @Column(name = "IMAGE_MAIN")
    val imageMain: String?,
    @Column(name = "STATUS")
    val status: String?,
    @Column(name = "BREAKTIME")
    val breakTime: String?,
    @Column(name = "ROAD_ADDRESS")
    val roadAddress: String?,
    @Column(name = "JIBUN_ADDRESS")
    val jibunAddress: String?,
    @Column(name = "CATEGORY")
    val category: String?,
)
