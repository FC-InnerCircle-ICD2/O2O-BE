package org.fastcampus.applicationadmin.review.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class OwnerReplyResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val date: LocalDate,
    val content: String,
)
