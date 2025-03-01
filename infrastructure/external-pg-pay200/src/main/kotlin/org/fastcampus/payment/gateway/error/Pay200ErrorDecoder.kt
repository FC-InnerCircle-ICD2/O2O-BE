package org.fastcampus.payment.gateway.error

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

/**
 * HTTP 응답 상태코드 2XX 범위 외 경우일 때.
 */
internal class Pay200ErrorDecoder(
    @Qualifier("feignObjectMapper")
    private val objectMapper: ObjectMapper,
) : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response?): Exception {
        logger.error("{} 에러", methodKey)
        logger.debug("{}", response?.body())
        try {
            val body = response?.body()?.asInputStream()?.readAllBytes()?.let { String(it, StandardCharsets.UTF_8) }
            if (body == null) {
                return Pay200Exception("", Pay200ApproveErrorResponse())
            }
            val errorResponse = objectMapper.readValue(body, Pay200ApproveErrorResponse::class.java)
            return Pay200Exception(errorResponse.error.message, errorResponse)
        } catch (e: IOException) {
            return Pay200Exception(message = "에러 메세지 파싱 에러")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
