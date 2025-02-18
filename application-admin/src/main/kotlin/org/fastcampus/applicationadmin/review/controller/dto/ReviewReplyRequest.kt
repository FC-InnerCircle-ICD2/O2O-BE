package org.fastcampus.applicationadmin.review.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewReplyRequest
    @JsonCreator
    constructor(
        @JsonProperty("content") val content: String,
    )
