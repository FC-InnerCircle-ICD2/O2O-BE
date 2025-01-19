package org.fastcampus.applicationclient.member.service

import org.fastcampus.applicationclient.member.dto.MemberJoinRequest
import org.fastcampus.applicationclient.member.dto.MemberJoinResponse
import org.fastcampus.member.code.MemberState
import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by kms0902 on 25. 1. 19..
 */
@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun join(memberJoinRequest: MemberJoinRequest): MemberJoinResponse {
        val createMember =
            Member(
                null,
                Role.USER,
                MemberState.JOIN,
                requireNotNull(memberJoinRequest.signname),
                requireNotNull(memberJoinRequest.password),
                requireNotNull(memberJoinRequest.username),
                requireNotNull(memberJoinRequest.nickname),
                requireNotNull(memberJoinRequest.phone),
            )
        val savedMember = memberRepository.save(createMember)
        return MemberJoinResponse(savedMember?.id)
    }
}
