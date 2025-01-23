package org.fastcampus.applicationclient.config.security.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.fastcampus.applicationclient.config.security.dto.JwtDTO
import org.fastcampus.applicationclient.config.security.dto.LoginUser
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    fun createAccessToken(loginUser: LoginUser, secretKey: String): Pair<String, String> {
        val expirationTime = Date(System.currentTimeMillis() + JwtDTO.ACCESS_TOKEN_EXPIRATION_TIME)
        val token = JWT.create()
            .withSubject("accessToken")
            .withClaim("id", loginUser.member.id)
            .withClaim("role", loginUser.member.role.toString())
            .withClaim("state", loginUser.member.state.toString())
            .withExpiresAt(expirationTime)
            .sign(Algorithm.HMAC512(secretKey))
        return JwtDTO.TOKEN_PREFIX + token to expirationTime.toInstant().toString()
    }

    fun createRefreshToken(loginUser: LoginUser, secretKey: String): Pair<String, String> {
        val expirationTime = Date(System.currentTimeMillis() + JwtDTO.REFRESH_TOKEN_EXPIRATION_TIME)
        val token = JWT.create()
            .withSubject("refreshToken")
            .withClaim("id", loginUser.member.id)
            .withClaim("role", loginUser.member.role.toString())
            .withClaim("state", loginUser.member.state.toString())
            .withExpiresAt(expirationTime)
            .sign(Algorithm.HMAC512(secretKey))
        return JwtDTO.TOKEN_PREFIX + token to expirationTime.toInstant().toString()
    }

    fun verify(token: String, secretKey: String): LoginUser {
        val decodedJWT: DecodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
            .build()
            .verify(token)
        val id = decodedJWT.getClaim("id").asLong()
        val role = decodedJWT.getClaim("role").asString()
        val state = decodedJWT.getClaim("state").asString()
        val account = Member(
            id = id,
            role = Role.valueOf(role),
            state = MemberState.valueOf(state),
            "",
            "",
            "",
            "",
            "",
        )
        return LoginUser(account)
    }

    fun isTokenExpired(token: String, secretKey: String): Boolean {
        return try {
            val decodedJWT: DecodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
            decodedJWT.expiresAt.before(Date())
        } catch (e: Exception) {
            true // 만료되었거나, 잘못된 토큰일 경우
        }
    }
}
