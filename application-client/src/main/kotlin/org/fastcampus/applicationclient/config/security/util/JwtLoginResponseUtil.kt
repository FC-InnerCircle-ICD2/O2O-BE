package org.fastcampus.applicationclient.config.security.util

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.common.dto.APIResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

object JwtLoginResponseUtil {
    private val log = LoggerFactory.getLogger(JwtLoginResponseUtil::class.java)

    fun sendResponse(response: HttpServletResponse, httpStatus: HttpStatus, body: Any) {
        try {
            val objectMapper = ObjectMapper()
            val responseDto = APIResponseDTO(httpStatus.value(), null, body)
            val responseBody = objectMapper.writeValueAsString(responseDto)
            response.contentType = "application/json; charset=utf-8"
            response.status = httpStatus.value()
            response.writer.println(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("서버 파싱 에러")
        }
    }
}
