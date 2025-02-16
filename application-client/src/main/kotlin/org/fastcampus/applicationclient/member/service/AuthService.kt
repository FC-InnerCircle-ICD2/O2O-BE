package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.aop.MemberMetered
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.config.security.dto.JwtDTO
import org.fastcampus.applicationclient.config.security.dto.response.JwtLoginResponse
import org.fastcampus.applicationclient.config.security.service.JwtService
import org.fastcampus.applicationclient.member.dto.request.AuthLogoutRequest
import org.fastcampus.applicationclient.member.dto.request.AuthRefreshRequest
import org.fastcampus.applicationclient.member.dto.request.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.response.MemberJoinResponse
import org.fastcampus.applicationclient.member.exception.MemberException
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Created by kms0902 on 25. 1. 26..
 */
@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService,
    @Value("\${security.secret.key}")
    private val secretKey: String,
) {
    @MemberMetered
    fun join(memberJoinRequest: MemberJoinRequest): MemberJoinResponse {
        val role = Role.USER
        val createMember =
            Member(
                null,
                role,
                MemberState.JOIN,
                requireNotNull(memberJoinRequest.signname),
                requireNotNull(passwordEncoder.encode(memberJoinRequest.password)),
                requireNotNull(memberJoinRequest.username),
                requireNotNull(memberJoinRequest.nickname),
                requireNotNull(memberJoinRequest.phone),
            )
        val findMember = memberRepository.findByRoleAndSignname(role, requireNotNull(memberJoinRequest.signname))
        if (findMember != null && MemberState.JOIN == findMember.state) {
            throw MemberException(MemberExceptionResult.DUPLICATE_MEMBER)
        }

        val savedMember = memberRepository.save(createMember)
        val memberId = savedMember!!.id

        if (memberJoinRequest.address == null) {
            throw MemberException(MemberExceptionResult.INVALID_REQUIRED_ADDRESS)
        }
        val authMember = AuthMember(
            requireNotNull(memberId),
            role,
        )
        memberService.createAddress(memberJoinRequest.address, authMember)

        return MemberJoinResponse(savedMember.id)
    }

    @MemberMetered
    fun refresh(authRefreshRequest: AuthRefreshRequest): JwtLoginResponse {
        val accessToken = authRefreshRequest.accessToken.replace(JwtDTO.TOKEN_PREFIX, "").trim()
        val refreshToken = authRefreshRequest.refreshToken.replace(JwtDTO.TOKEN_PREFIX, "").trim()

        // token 만료 검증
        if (jwtService.isTokenExpired(refreshToken, secretKey)) {
            throw MemberException(MemberExceptionResult.TOKEN_EXPIRED)
        }

        // refresh token 검증
        if (jwtService.isAccessToken(refreshToken, secretKey)) {
            throw MemberException(MemberExceptionResult.INVALID_REFRESH_TOKEN)
        }

        val loginUser = jwtService.verify(refreshToken, secretKey)

        val role = loginUser.getRole()
        if (Role.USER != role) {
            throw MemberException(MemberExceptionResult.FORBIDDEN_ACCESS)
        }

        val memberId = requireNotNull(loginUser.getId())
        val validateRefreshToken = jwtService.validateRefreshToken(memberId, refreshToken)
        if (!validateRefreshToken) {
            throw MemberException(MemberExceptionResult.TOKEN_EXPIRED)
        }

        // 블랙리스트 토큰 검증
        val isBlackListToken = jwtService.isBlackListToken(refreshToken)
        if (isBlackListToken) {
            throw MemberException(MemberExceptionResult.TOKEN_EXPIRED)
        }

        // 기존 access token과 refresh token 만료 처리
        jwtService.saveBlackList(accessToken)
        jwtService.saveBlackList(refreshToken)

        // 신규 access token, refresh token 생성
        val (createdAccessToken, accessTokenExpiration) = jwtService.createAccessToken(loginUser, secretKey)
        val (createdRefreshToken, refreshTokenExpiration) = jwtService.createRefreshToken(loginUser, secretKey)

        return JwtLoginResponse(createdAccessToken, createdRefreshToken, accessTokenExpiration, refreshTokenExpiration)
    }

    @MemberMetered
    fun logout(authLogoutRequest: AuthLogoutRequest) {
        val accessToken = authLogoutRequest.accessToken.replace(JwtDTO.TOKEN_PREFIX, "").trim()
        val refreshToken = authLogoutRequest.refreshToken.replace(JwtDTO.TOKEN_PREFIX, "").trim()
        jwtService.saveBlackList(accessToken)
        jwtService.saveBlackList(refreshToken)
    }
}
