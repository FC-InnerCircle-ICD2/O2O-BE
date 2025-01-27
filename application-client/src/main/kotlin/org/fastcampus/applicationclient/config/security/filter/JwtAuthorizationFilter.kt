package org.fastcampus.applicationclient.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtAuthenticated
import org.fastcampus.applicationclient.config.security.dto.JwtDTO
import org.fastcampus.applicationclient.config.security.provider.ApplicationContextProvider
import org.fastcampus.applicationclient.config.security.service.JwtService
import org.fastcampus.applicationclient.config.security.util.JwtLoginResponseUtil
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val secretKey: String,
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // @JwtAuthenticated 검증
        val handlerMethod = getHandlerMethod(request)
        val requiresAuth = handlerMethod?.hasMethodAnnotation(JwtAuthenticated::class.java) ?: false
        if (!requiresAuth) {
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
            sendResponse(response, MemberExceptionResult.TOKEN_EXPIRED)
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

    private fun getHandlerMethod(request: HttpServletRequest): HandlerMethod? {
        val requestMappingHandlerMapping =
            ApplicationContextProvider.getApplicationContext().getBean("requestMappingHandlerMapping") as RequestMappingHandlerMapping

        return try {
            requestMappingHandlerMapping.getHandler(request)?.handler as? HandlerMethod
        } catch (e: Exception) {
            null
        }
    }

    private fun sendResponse(response: HttpServletResponse, exceptionEnum: MemberExceptionResult) {
        JwtLoginResponseUtil.sendResponse(
            response,
            exceptionEnum.httpStatus,
            mapOf("error" to exceptionEnum.message),
        )
    }
}
