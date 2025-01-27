package org.fastcampus.store.redis.repository

import org.fastcampus.member.repository.MemberRedisRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class MemberRedisRepositoryImpl(
    val redisTemplate: RedisTemplate<String, Any>,
) : MemberRedisRepository {
    override fun saveToken(
        memberId: Long,
        tokenType: String,
        token: String,
        refreshTokenExpirationTime: Int,
        milliseconds: TimeUnit,
    ) {
        redisTemplate.opsForValue().set(
            "$tokenType$memberId",
            token,
            refreshTokenExpirationTime.toLong(),
            milliseconds,
        )
    }

    override fun saveBlackList(token: String, refreshTokenExpirationTime: Int, milliseconds: TimeUnit) {
        redisTemplate.opsForValue().set(
            "BLACKLIST:$token",
            true,
            refreshTokenExpirationTime.toLong(),
            milliseconds,
        )
    }

    override fun getToken(memberId: Long, tokenType: String, token: String): String {
        return redisTemplate.opsForValue().get("$tokenType$memberId").toString()
    }

    override fun getBlackList(token: String): Boolean {
        return redisTemplate.opsForValue().get("BLACKLIST:$token")?.let { it as Boolean } ?: false
    }
}
