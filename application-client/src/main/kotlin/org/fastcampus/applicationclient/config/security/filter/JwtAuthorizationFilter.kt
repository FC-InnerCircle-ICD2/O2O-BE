package org.fastcampus.applicationclient.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtDTO
import org.fastcampus.applicationclient.config.security.service.JwtService
import org.fastcampus.applicationclient.config.security.util.JwtLoginResponseUtil
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val secretKey: String,
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (isHeaderVerify(request)) {
            val token = request.getHeader(JwtDTO.HEADER).replace(JwtDTO.TOKEN_PREFIX, "")

            val jwtService = JwtService()
            if (jwtService.isTokenExpired(token, secretKey)) {
                JwtLoginResponseUtil.sendResponse(response, HttpStatus.UNAUTHORIZED, mapOf("error" to "토큰이 만료되었습니다."))
                return
            }

            val loginUser = JwtService().verify(token, secretKey)

            val authMember = AuthMember(
                id = requireNotNull(loginUser.member.id),
                role = loginUser.member.role,
                state = loginUser.member.state,
            )

            loginUser.member.let {
                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    authMember,
                    null,
                    loginUser.authorities,
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }

    private fun isHeaderVerify(request: HttpServletRequest): Boolean {
        val header = request.getHeader(JwtDTO.HEADER)
        return header != null && header.startsWith(JwtDTO.TOKEN_PREFIX)
    }
}
