package org.fastcampus.applicationadmin.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.applicationadmin.config.security.dto.AuthMember
import org.fastcampus.applicationadmin.config.security.dto.JwtDTO
import org.fastcampus.applicationadmin.config.security.service.JwtService
import org.fastcampus.applicationadmin.config.security.util.JwtLoginResponseUtil
import org.fastcampus.applicationadmin.member.exception.MemberExceptionResult
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val secretKey: String,
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestURI = request.requestURI
        // 특정 URL 제외
        if (requestURI == "/api/v1/auth/refresh" || requestURI == "/api/v1/auth/logout") {
            chain.doFilter(request, response)
            return
        }

        val header = request.getHeader(JwtDTO.HEADER)

        // 헤더 검증
        if (header == null || jwtService.validateHeader(header)) {
            sendResponse(response, MemberExceptionResult.HEADER_TOKEN_EMPTY)
            return
        }

        val token = jwtService.getHeaderToken(header)

        // token 만료 검증
        if (jwtService.isTokenExpired(token, secretKey)) {
            JwtLoginResponseUtil.sendResponse(response, HttpStatus.UNAUTHORIZED, mapOf("error" to "토큰이 만료되었습니다."))
            return
        }

        // access token 검증
        if (!jwtService.isAccessToken(token, secretKey)) {
            sendResponse(response, MemberExceptionResult.INVALID_ACCESS_TOKEN)
            return
        }

        // 블랙 리스트 토큰 검증
        val isBlacklisted = jwtService.isBlackListToken(token)
        if (isBlacklisted) {
            sendResponse(response, MemberExceptionResult.TOKEN_EXPIRED)
            return
        }

        // 유효한 access token일 경우, 인증 객체를 설정
        val loginUser = jwtService.verify(token, secretKey)
        val authMember = AuthMember(
            id = requireNotNull(loginUser.member.id),
            role = loginUser.member.role,
        )

        loginUser.member.let {
            val authentication: Authentication = UsernamePasswordAuthenticationToken(
                authMember,
                null,
                loginUser.authorities,
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }

    private fun sendResponse(response: HttpServletResponse, exceptionEnum: MemberExceptionResult) {
        JwtLoginResponseUtil.sendResponse(
            response,
            exceptionEnum.httpStatus,
            mapOf("error" to exceptionEnum.message),
        )
    }
}
