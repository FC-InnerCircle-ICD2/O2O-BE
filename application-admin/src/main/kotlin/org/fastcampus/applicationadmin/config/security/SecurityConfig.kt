package org.fastcampus.applicationadmin.config.security

import org.fastcampus.applicationadmin.config.security.filter.JwtAuthenticationFilter
import org.fastcampus.applicationadmin.config.security.filter.JwtAuthorizationFilter
import org.fastcampus.applicationadmin.config.security.util.JwtLoginResponseUtil
import org.fastcampus.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.encrypt.AesBytesEncryptor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Created by kms0902 on 25. 1. 20..
 */
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig(
    @Value("\${security.secret.key}")
    private val secretKey: String,
    private val memberRepository: MemberRepository,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun aesBytesEncryptor(): AesBytesEncryptor = AesBytesEncryptor(secretKey, "70726574657374")

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        val jwtAuthenticationFilter = JwtAuthenticationFilter(authenticationManager, secretKey, memberRepository)
        val jwtAuthorizationFilter = JwtAuthorizationFilter(authenticationManager, secretKey)

        http.headers { it.frameOptions { frame -> frame.sameOrigin() } }
        http.csrf { it.disable() }
        http.cors { it.configurationSource(configurationSource()) }
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.formLogin { it.disable() }
        http.httpBasic { it.disable() }

        http.addFilter(jwtAuthenticationFilter)
        http.addFilter(jwtAuthorizationFilter)

        http.exceptionHandling {
            it.authenticationEntryPoint { _, response, _ ->
                val errors = mapOf("request" to "로그인이 필요합니다.")
                JwtLoginResponseUtil.sendResponse(response, HttpStatus.UNAUTHORIZED, errors)
            }

            it.accessDeniedHandler { _, response, _ ->
                val errors = mapOf("request" to "접근 권한이 없습니다.")
                JwtLoginResponseUtil.sendResponse(response, HttpStatus.UNAUTHORIZED, errors)
            }
        }

        http.authorizeHttpRequests {
            it.anyRequest().authenticated()
        }

        return http.build()
    }

    @Bean
    fun configurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            addAllowedHeader("*")
            addAllowedMethod("*")
            addAllowedOriginPattern("*")
            allowCredentials = true
            addExposedHeader("Authorization")
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
