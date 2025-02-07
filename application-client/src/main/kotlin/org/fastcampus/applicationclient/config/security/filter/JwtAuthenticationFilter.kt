package org.fastcampus.applicationclient.config.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.fastcampus.applicationclient.config.security.dto.LoginUser
import org.fastcampus.applicationclient.config.security.dto.request.JwtLoginRequest
import org.fastcampus.applicationclient.config.security.dto.response.JwtLoginResponse
import org.fastcampus.applicationclient.config.security.service.JwtService
import org.fastcampus.applicationclient.config.security.util.JwtLoginResponseUtil
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
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
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val secretKey: String,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setFilterProcessesUrl("/api/v1/auth/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val validator: Validator = Validation.buildDefaultValidatorFactory().validator

        return try {
            val requestBody = request.inputStream.bufferedReader().use { it.readText() }

            val objectMapper = ObjectMapper()
            val loginRequest: JwtLoginRequest = objectMapper.readValue(requestBody, JwtLoginRequest::class.java)

            // 유효성 검증
            val violations = validator.validate(loginRequest)
            if (violations.isNotEmpty()) {
                val errorMessages = violations.joinToString(", ") { it.message }
                throw ConstraintViolationException(errorMessages, violations)
            }

            val role = Role.USER
            memberRepository.findByRoleAndSignname(role, requireNotNull(loginRequest.signname))
                ?: throw InternalAuthenticationServiceException(MemberExceptionResult.NOT_FOUND_MEMBER.message)

            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequest.signname,
                loginRequest.password,
                listOf(GrantedAuthority { "ROLE_$role" }),
            )
            authenticationManager.authenticate(authenticationToken)
        } catch (e: MismatchedInputException) {
            throw InternalAuthenticationServiceException("아이디 또는 비밀번호를 입력해 주세요", e)
        } catch (e: Exception) {
            throw InternalAuthenticationServiceException(e.message, e)
        }
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        val errorMessage = when {
            failed.message.equals("자격 증명에 실패하였습니다.") -> "아이디 또는 비밀번호가 일치하지 않습니다."
            else -> failed.message
        }

        val errors = mapOf("error" to errorMessage)
        val firstErrorMessage = errors.values.firstOrNull() ?: "Invalid request"
        JwtLoginResponseUtil.sendErrorResponse(response, HttpStatus.BAD_REQUEST, firstErrorMessage)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        val loginUser = authResult.principal as LoginUser
        val (accessToken, accessTokenExpiration) = jwtService.createAccessToken(loginUser, secretKey)
        val (refreshToken, refreshTokenExpiration) = jwtService.createRefreshToken(loginUser, secretKey)

        val responseBody = JwtLoginResponse(
            accessToken,
            refreshToken,
            accessTokenExpiration,
            refreshTokenExpiration,
        )
        JwtLoginResponseUtil.sendResponse(response, HttpStatus.OK, responseBody)
    }
}
