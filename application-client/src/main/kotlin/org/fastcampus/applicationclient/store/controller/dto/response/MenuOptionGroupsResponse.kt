package org.fastcampus.applicationclient.store.controller.dto.response

data class MenuResponse(
    val menuId: String,
    val name: String,
    val price: String,
    val desc: String,
    val imgUrl: String,
    val isSoldOut: Boolean,
    val isBest: Boolean,
    val isManyOrder: Boolean,
    val menuOptionGroups: List<MenuOptionGroupsResponse>,
)

data class MenuOptionGroupsResponse(
    val id: String,
    val name: String,
    val type: String,
    val minSel: Long,
    val maxSel: Long,
    val options: List<MenuOptionResponse>,
)

data class MenuOptionResponse(
    val id: String,
    val name: String,
    val price: Long,
    val isSoldOut: Boolean,
)
