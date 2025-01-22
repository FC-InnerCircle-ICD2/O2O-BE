package org.fastcampus.applicationadmin.sse

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Component
class SseManager {
    fun manage(key: String, emitter: SseEmitter) {
        emitter.onError {
            logger.error("SSE onError [{}]", key)
        }
        emitter.onTimeout {
            logger.debug("SSE onTimeout [{}]", key)
        }
        emitter.onCompletion {
            logger.debug("SSE onCompletion [{}]", key)
            emitters.remove(key)
        }
        emitters[key] = emitter

        // 연결 시작시 연결 지속을 위해 더미데이터 응답 1회
        push(
            key = key,
            comment = "server hello",
            reconnectTime = 1_000L,
        )
        logger.debug("관리중 세션: {}", emitters)
    }

    fun push(
        key: String,
        eventType: String? = null,
        data: String? = null,
        comment: String? = null,
        lastEventId: String? = null,
        reconnectTime: Long? = null,
    ) {
        try {
            emitters[key]?.send(
                SseEmitter
                    .event()
                    .apply {
                        eventType?.let { name(eventType) }
                        data?.let { data(data) }
                        comment?.let { comment(comment) }
                        lastEventId?.let { id(lastEventId) }
                        reconnectTime?.let { reconnectTime(reconnectTime) }
                    },
            ) ?: logger.debug("key[{}] 미존재", key)
        } catch (ex: Exception) {
            logger.error("SSE push error", ex)
            emitters.remove(key)
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(SseManager::class.java)
        private val emitters = ConcurrentHashMap<String, SseEmitter>()
    }
}
