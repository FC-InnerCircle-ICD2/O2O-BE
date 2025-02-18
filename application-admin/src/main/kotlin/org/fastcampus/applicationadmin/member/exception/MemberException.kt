package org.fastcampus.applicationadmin.member.exception

data class MemberException(
    val memberExceptionResult: MemberExceptionResult,
) : RuntimeException()
