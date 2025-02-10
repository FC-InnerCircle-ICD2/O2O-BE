package org.fastcampus.applicationclient.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OrderMetered(
    val version: String = "v1",
)
