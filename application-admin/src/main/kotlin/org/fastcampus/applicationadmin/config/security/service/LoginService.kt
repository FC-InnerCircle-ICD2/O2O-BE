package org.fastcampus.applicationadmin.config.security.service

import org.fastcampus.applicationadmin.config.security.dto.LoginUser
import org.fastcampus.member.entity.Member
import org.fastcampus.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {
    override fun loadUserByUsername(signname: String): UserDetails {
        val member: Member = memberRepository.findBySignname(signname)
        return LoginUser(member)
    }
}
