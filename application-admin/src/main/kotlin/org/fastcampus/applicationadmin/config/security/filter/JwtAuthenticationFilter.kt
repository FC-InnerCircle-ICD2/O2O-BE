package org.fastcampus.applicationadmin.config.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.applicationadmin.config.security.dto.LoginUser
import org.fastcampus.applicationadmin.config.security.dto.request.JwtLoginRequest
import org.fastcampus.applicationadmin.config.security.service.JwtService
import org.fastcampus.applicationadmin.config.security.util.JwtLoginResponseUtil
import org.fastcampus.member.code.Role
import org.fastcampus.member.repository.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Created by kms0902 on 25. 1. 20..
 */
class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val secretKey: String,
    private val memberRepository: MemberRepository,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setFilterProcessesUrl("/api/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return try {
            val requestBody = request.inputStream.bufferedReader().use { it.readText() }

            val objectMapper = ObjectMapper()
            val loginRequest: JwtLoginRequest = objectMapper.readValue(requestBody, JwtLoginRequest::class.java)

            val role = Role.CEO
            memberRepository.findByRoleAndSignname(role, requireNotNull(loginRequest.signname))

            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequest.signname,
                loginRequest.password,
                listOf(GrantedAuthority { "ROLE_$role" }),
            )
            authenticationManager.authenticate(authenticationToken)
        } catch (e: InvalidDefinitionException) {
            throw InternalAuthenticationServiceException("아이디 또는 비밀번호를 입력해 주세요", e)
        } catch (e: Exception) {
            throw InternalAuthenticationServiceException(e.message ?: "Authentication failed", e)
        }
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        val errorMessage = when (failed) {
            is InternalAuthenticationServiceException -> "아이디 또는 비밀번호가 일치하지 않습니다."
            else -> failed.message
        }

        val errors = mapOf("error" to errorMessage)
        JwtLoginResponseUtil.sendResponse(response, HttpStatus.BAD_REQUEST, errors)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        val loginUser = authResult.principal as LoginUser
        val jwtService = JwtService()

        val (accessToken, accessTokenExpiration) = jwtService.createAccessToken(loginUser, secretKey)
        val (refreshToken, refreshTokenExpiration) = jwtService.createRefreshToken(loginUser, secretKey)

        val responseBody = mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken,
            "accessTokenExpiresIn" to accessTokenExpiration,
            "refreshTokenExpiresIn" to refreshTokenExpiration,
        )

        JwtLoginResponseUtil.sendResponse(response, HttpStatus.OK, responseBody)
    }
}
