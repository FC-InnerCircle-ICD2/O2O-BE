package org.fastcampus.applicationadmin.config.security.dto

import org.fastcampus.member.code.Role
import org.fastcampus.member.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class LoginUser(val member: Member) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { "ROLE_${member.role}" })
    }

    fun getRole(): Role {
        return member.role
    }

    override fun getUsername(): String {
        return member.signname
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
