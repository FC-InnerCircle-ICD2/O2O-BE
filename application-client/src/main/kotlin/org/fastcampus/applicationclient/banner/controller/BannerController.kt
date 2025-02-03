package org.fastcampus.applicationclient.banner.controller

import org.fastcampus.applicationclient.banner.controller.dto.response.BannerResponse
import org.fastcampus.common.dto.APIResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/banners")
class BannerController {
    @GetMapping
    fun getBanners(): ResponseEntity<APIResponseDTO<List<BannerResponse>>> {
        val response: List<BannerResponse> = listOf(
            BannerResponse(
                id = 1,
                imageUrl = "/images/banner/banner_1.png",
                link = "/event/1",
                title = "첫 주문 시 5천원 할인",
            ),
            BannerResponse(
                id = 2,
                imageUrl = "/images/banner/banner_1.png",
                link = "/event/2",
                title = "신규 가게 오픈 이벤트",
            ),
        )
        return ResponseEntity
            .ok(APIResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, response))
    }
}
