package org.fastcampus.member.repository

import java.util.concurrent.TimeUnit

interface MemberRedisRepository {
    fun saveToken(
        memberId: Long,
        tokenType: String,
        token: String,
        refreshTokenExpirationTime: Int,
        milliseconds: TimeUnit,
    )

    fun saveBlackList(token: String, refreshTokenExpirationTime: Int, milliseconds: TimeUnit)

    fun getToken(memberId: Long, tokenType: String, token: String): String

    fun getBlackList(token: String): Boolean
}
