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
    fun create(loginUser: LoginUser, secretKey: String): String {
        val jwtToken = JWT.create()
            .withSubject("jwt")
            .withClaim("id", loginUser.member.id)
            .withClaim("role", loginUser.member.role.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + JwtDTO.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(secretKey))
        return JwtDTO.TOKEN_PREFIX + jwtToken
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
            MemberState.JOIN,
            "",
            "",
            "",
            "",
            "",
        )
        return LoginUser(account)
    }
}
