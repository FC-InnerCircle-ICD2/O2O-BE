package org.fastcampus.applicationclient.store.controller.dto.response

data class MenuOptionGroupsResponse(
    val id: String,
    val name: String,
    val minSel: String,
    val maxSel: String,
    val order: Long,
    val menuOptions: List<MenuOptionResponse>,
)

data class MenuOptionResponse(
    val id: String,
    val name: String,
    val price: String,
    val isSoldOut: Boolean,
    val order: Long,
)
