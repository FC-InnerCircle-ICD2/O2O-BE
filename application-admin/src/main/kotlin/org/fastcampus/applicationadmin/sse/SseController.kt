package org.fastcampus.applicationadmin.sse

import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.sse.docs.SseControllerDocs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api")
class SseController(
    private val sseManager: SseManager,
) : SseControllerDocs {
    @GetMapping("/v1/event-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    override fun connectSse(
        @AuthenticationPrincipal authMember: AuthMember,
    ): SseEmitter {
        logger.debug("SSE Connecting UserId: {}", authMember.id)

        val emitter = SseEmitter(TIMEOUT_MILLIS)
        sseManager.manage(authMember.id.toString(), emitter)
        return emitter
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SseController::class.java)
        private const val TIMEOUT_MILLIS = 65 * 1_000L
    }
}
