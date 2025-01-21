package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.member.dto.request.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.response.MemberJoinResponse
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
    fun join(memberJoinRequest: MemberJoinRequest): MemberJoinResponse {
        val createMember =
            Member(
                null,
                Role.USER,
                MemberState.JOIN,
                requireNotNull(memberJoinRequest.signname),
                requireNotNull(passwordEncoder.encode(memberJoinRequest.password)),
                requireNotNull(memberJoinRequest.username),
                requireNotNull(memberJoinRequest.nickname),
                requireNotNull(memberJoinRequest.phone),
            )
        val savedMember = memberRepository.save(createMember)
        return MemberJoinResponse(savedMember?.id)
    }
}
