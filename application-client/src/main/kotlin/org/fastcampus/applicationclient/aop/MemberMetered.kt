package org.fastcampus.applicationclient.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberMetered(
    val version: String = "v1",
)
