package org.fastcampus.store.entity

data class CustomPage<T>(
    val content: List<T>, // 현재 페이지의 데이터
    val currentPage: Int, // 현재 페이지 번호
    val totalPages: Int, // 전체 페이지 수
    val totalItems: Long, // 전체 아이템 수
    val hasNext: Boolean, // 다음 페이지 존재 여부
)
