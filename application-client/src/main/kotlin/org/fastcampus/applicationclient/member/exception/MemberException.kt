package org.fastcampus.applicationclient.member.exception

data class MemberException(
    val memberExceptionResult: MemberExceptionResult,
) : RuntimeException()
