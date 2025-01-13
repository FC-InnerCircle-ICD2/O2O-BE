package org.fastcampus.store.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.fastcampus.common.entity.BaseEntity

@Entity
@Table(name = "TB_STORE_MENU_CATEGORY")
class StoreMenuCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long? = null,
    @Column(name = "NAME")
    val name: String?,
    @Column(name = "STORE_ID")
    val storeId: Long?,
) : BaseEntity()
