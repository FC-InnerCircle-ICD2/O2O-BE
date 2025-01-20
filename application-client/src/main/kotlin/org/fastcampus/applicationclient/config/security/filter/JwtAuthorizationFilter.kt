package org.fastcampus.applicationclient.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fastcampus.applicationclient.config.security.dto.JwtDTO
import org.fastcampus.applicationclient.config.security.service.JwtService
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
            val loginUser = JwtService().verify(token, secretKey)
            loginUser.member.let {
                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    loginUser,
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
