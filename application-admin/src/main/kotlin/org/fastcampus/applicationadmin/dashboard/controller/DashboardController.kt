package org.fastcampus.applicationadmin.dashboard.controller

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.dashboard.dto.response.DashboardResponse
import org.fastcampus.applicationadmin.dashboard.dto.response.Type
import org.fastcampus.applicationadmin.dashboard.service.DashboardService
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController(
    private val service: DashboardService,
) {
    @GetMapping
    fun getDashboard(
        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") startDate: LocalDate,
        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") endDate: LocalDate,
        @RequestParam(required = true) type: Type,
        @AuthenticationPrincipal authMember: AuthMember,
    ): ResponseEntity<APIResponseDTO<DashboardResponse>> =
        ResponseEntity.ok(
            APIResponseDTO(
                HttpStatus.OK.value(),
                HttpStatus.OK.reasonPhrase,
                service.getDashboard(authMember.id, startDate, endDate, type.name),
            ),
        )
}
