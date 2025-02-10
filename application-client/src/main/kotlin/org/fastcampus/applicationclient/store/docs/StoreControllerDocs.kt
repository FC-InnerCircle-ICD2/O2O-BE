package org.fastcampus.applicationclient.store.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.fastcampus.applicationclient.store.controller.dto.request.SearchRequest
import org.fastcampus.applicationclient.store.controller.dto.response.CategoryInfo
import org.fastcampus.applicationclient.store.controller.dto.response.MenuResponse
import org.fastcampus.applicationclient.store.controller.dto.response.StoreInfo
import org.fastcampus.applicationclient.store.controller.dto.response.TrendKeywordsResponse
import org.fastcampus.common.dto.APIResponseDTO
import org.fastcampus.common.dto.CursorDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "스토어 API", description = "스토어 API 입니다.")
interface StoreControllerDocs {
    @Operation(
        summary = "스토어 상세 정보 조회",
        description = "특정 스토어의 상세 정보를 조회합니다.",
        parameters = [
            Parameter(name = "id", description = "스토어 ID", required = true, example = "1006816630"),
            Parameter(
                name = "X-User-Lat",
                description = "사용자의 위도",
                `in` = ParameterIn.HEADER,
                required = true,
                example = "37.5665",
            ),
            Parameter(
                name = "X-User-Lng",
                description = "사용자의 경도",
                `in` = ParameterIn.HEADER,
                required = true,
                example = "126.9780",
            ),
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "ok",
                content = [Content(schema = Schema(implementation = StoreInfo::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "스토어 상세 정보 조회 실패",
            ),
        ],
    )
    fun getStoreDetails(
        @PathVariable id: String,
        @RequestHeader("X-User-Lat") userLat: Double,
        @RequestHeader("X-User-Lng") userLng: Double,
    ): ResponseEntity<APIResponseDTO<StoreInfo>>

    @Operation(
        summary = "메뉴 옵션 조회",
        description = "해당 스토어의 특정 메뉴 옵션 정보를 가져옵니다. best, manyOrder 값은 DB 추가 시 다른 로직에도 영향이 가기 때문에 현재는 랜덤 값으로 전달드립니다.",
        parameters = [
            Parameter(name = "storeId", description = "스토어 ID", required = true, example = "1006816630"),
            Parameter(name = "menuId", description = "메뉴 ID", required = true, example = "54e57531-ad59-47c4-b7b0-4258ef2c4d62"),
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "ok",
                content = [Content(schema = Schema(implementation = MenuResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "스토어 메뉴 옵션 조회 실패",
            ),
        ],
    )
    fun getMenusOptions(
        @PathVariable storeId: String,
        @PathVariable menuId: String,
    ): APIResponseDTO<MenuResponse>

    @Operation(
        summary = "스토어 카테고리 & 메뉴 조회",
        description = "해당 스토어의 메뉴 카테고리 리스트를 조회합니다. 페이징 처리를 지원합니다.",
        parameters = [
            Parameter(name = "id", description = "스토어 ID", required = true, example = "1006816630"),
            Parameter(
                name = "page",
                description = "페이지 번호 (기본값: 1)",
                required = false,
                example = "1",
            ),
            Parameter(
                name = "size",
                description = "한 페이지당 가져올 데이터 개수 (기본값: 5)",
                required = false,
                example = "5",
            ),
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "ok",
                content = [Content(schema = Schema(implementation = CursorDTO::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "스토어 카테고리 조회 실패",
            ),
        ],
    )
    fun getCategories(
        @PathVariable id: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<CursorDTO<CategoryInfo>>

    @Operation(
        summary = "검색어 자동완성",
        description = "사용자가 입력한 검색어를 기반으로 스토어 추천 검색어를 반환합니다. 페이징 처리를 지원합니다.",
        parameters = [
            Parameter(
                name = "affix",
                description = "검색어 접미사 (검색어의 일부 입력값)",
                required = false,
                example = "치킨",
            ),
            Parameter(
                name = "page",
                description = "페이지 번호 (기본값: 1)",
                required = false,
                example = "1",
            ),
            Parameter(
                name = "size",
                description = "한 페이지당 가져올 데이터 개수 (기본값: 5)",
                required = false,
                example = "5",
            ),
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "ok",
                content = [Content(schema = Schema(implementation = CursorDTO::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "스토어 추천 검색어 조회 실패",
            ),
        ],
    )
    fun getStoreSuggestion(
        @RequestParam(defaultValue = "") affix: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<APIResponseDTO<CursorDTO<String>>>

    @Operation(
        summary = "실시간 급상승 검색어",
        description = "현재 인기 있는 트렌드 검색어 목록을 반환합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "ok",
                content = [Content(schema = Schema(implementation = TrendKeywordsResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "트렌드 검색어 조회 실패",
            ),
        ],
    )
    fun getTrendKeywords(): ResponseEntity<APIResponseDTO<TrendKeywordsResponse>>?

    @Operation(
        summary = "스토어 검색",
        description = "사용자가 입력한 키워드를 기반으로 스토어를 검색합니다.",
        requestBody = RequestBody(
            description = "검색 요청 (키워드 입력)",
            required = true,
            content = [Content(schema = Schema(implementation = SearchRequest::class))],
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "검색 성공",
                content = [Content(schema = Schema(implementation = APIResponseDTO::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "검색 실패",
            ),
        ],
    )
    fun search(
        @RequestBody keyword: String,
    ): ResponseEntity<APIResponseDTO<String>>
}
