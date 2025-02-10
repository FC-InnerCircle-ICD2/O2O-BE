package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.aop.MemberMetered
import org.fastcampus.applicationclient.config.security.dto.AuthMember
import org.fastcampus.applicationclient.member.dto.request.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.response.MemberInfoResponse
import org.fastcampus.applicationclient.member.dto.response.MemberJoinResponse
import org.fastcampus.applicationclient.member.exception.MemberException
import org.fastcampus.applicationclient.member.exception.MemberExceptionResult
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Created by kms0902 on 25. 1. 19..
 */
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
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
        return MemberJoinResponse(savedMember?.id)
    }

    @MemberMetered
    fun info(authMember: AuthMember): MemberInfoResponse? {
        val findMember = memberRepository.findById(authMember.id)
        return MemberInfoResponse(findMember.signname, findMember.nickname)
    }
}
