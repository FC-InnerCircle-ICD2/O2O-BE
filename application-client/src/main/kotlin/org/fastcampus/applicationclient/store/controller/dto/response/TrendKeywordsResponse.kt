package org.fastcampus.applicationclient.store.controller.dto.response

data class TrendKeywordsResponse(
    val trendKeywords: List<TrendKeyword>,
) {
    data class TrendKeyword(
        val keyword: String,
        val order: Int,
    )
}
