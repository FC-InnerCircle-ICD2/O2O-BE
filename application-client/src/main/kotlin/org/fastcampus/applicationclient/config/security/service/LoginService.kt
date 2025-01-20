package org.fastcampus.applicationclient.config.security.service

import org.fastcampus.applicationclient.config.security.dto.LoginUser
import org.fastcampus.applicationclient.config.security.exception.UserNotFoundException
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
//        val member: Member = memberRepository.findBySignname(signname) ?: throw UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다")
        val member: Member = memberRepository.findBySignname(signname).orElseThrow { UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다") }
        return LoginUser(member)
    }
}
