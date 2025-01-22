package org.fastcampus.applicationadmin.sse

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/v1")
class SseController(
    private val sseManager: SseManager,
) {
    @GetMapping("/event-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun connectSse(): SseEmitter {
        // TODO ID 받기
        logger.debug("connectSse")

        // ID로 store 조회, key 설정
        val emitter = SseEmitter(TIMEOUT_MILLIS)
        sseManager.manage("temp", emitter)
        return emitter
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SseController::class.java)
        private const val TIMEOUT_MILLIS = 60 * 1_000L
    }
}
