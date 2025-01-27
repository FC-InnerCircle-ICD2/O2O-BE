package org.fastcampus.applicationadmin.config.security.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.fastcampus.applicationadmin.config.security.code.TokenType
import org.fastcampus.applicationadmin.config.security.dto.JwtDTO
import org.fastcampus.applicationadmin.config.security.dto.LoginUser
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRedisRepository
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class JwtService(
    private val memberRedisRepository: MemberRedisRepository,
) {
    fun createAccessToken(loginUser: LoginUser, secretKey: String): Pair<String, String> {
        val expirationTime = Date(System.currentTimeMillis() + JwtDTO.ACCESS_TOKEN_EXPIRATION_TIME)
        val token =
            JWT.create().withSubject("accessToken").withClaim("id", loginUser.member.id).withClaim("role", loginUser.member.role.toString())
                .withExpiresAt(expirationTime).sign(Algorithm.HMAC512(secretKey))
        return JwtDTO.TOKEN_PREFIX + token to expirationTime.toInstant().toString()
    }

    fun createRefreshToken(loginUser: LoginUser, secretKey: String): Pair<String, String> {
        val refreshTokenExpirationTime = JwtDTO.REFRESH_TOKEN_EXPIRATION_TIME
        val expirationTime = Date(System.currentTimeMillis() + refreshTokenExpirationTime)

        val memberId = requireNotNull(loginUser.member.id)
        val token = JWT.create().withSubject("refreshToken").withClaim("id", memberId).withClaim("role", loginUser.member.role.toString())
            .withExpiresAt(expirationTime).sign(Algorithm.HMAC512(secretKey))

        saveTokenToRedis(memberId, token)

        return JwtDTO.TOKEN_PREFIX + token to expirationTime.toInstant().toString()
    }

    private fun saveTokenToRedis(memberId: Long, token: String) {
        memberRedisRepository.saveToken(
            memberId,
            TokenType.REFRESH_TOKEN.tokenName,
            token,
            JwtDTO.REFRESH_TOKEN_EXPIRATION_TIME,
            TimeUnit.MILLISECONDS,
        )
    }

    fun saveBlackList(token: String) {
        memberRedisRepository.saveBlackList(token, JwtDTO.REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS)
    }

    fun validateRefreshToken(memberId: Long, refreshToken: String): Boolean {
        val storedToken = memberRedisRepository.getToken(memberId, TokenType.REFRESH_TOKEN.tokenName, refreshToken)
        return storedToken == refreshToken
    }

    fun isBlackListToken(token: String): Boolean {
        return memberRedisRepository.getBlackList(token)
    }

    fun verify(token: String, secretKey: String): LoginUser {
        val decodedJWT: DecodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
            .build()
            .verify(token)
        val id = decodedJWT.getClaim("id").asLong()
        val role = decodedJWT.getClaim("role").asString()
        val account = Member(
            id = id,
            role = Role.valueOf(role),
            null,
            "",
            "",
            "",
            "",
            "",
        )
        return LoginUser(account)
    }

    fun validateHeader(header: String): Boolean {
        return !header.startsWith(JwtDTO.TOKEN_PREFIX)
    }

    fun getHeaderToken(header: String): String {
        return header.replace(JwtDTO.TOKEN_PREFIX, "").trim()
    }

    fun isAccessToken(token: String, secretKey: String): Boolean {
        return decodedJWT(secretKey, token).subject.equals("accessToken")
    }

    fun isTokenExpired(token: String, secretKey: String): Boolean {
        return try {
            val decodedJWT = decodedJWT(secretKey, token)
            decodedJWT.expiresAt.before(Date())
        } catch (e: Exception) {
            true // 만료되었거나, 잘못된 토큰일 경우
        }
    }

    private fun decodedJWT(secretKey: String, token: String): DecodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
}
