package org.fastcampus.applicationclient.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReviewMetered(
    val version: String = "v1",
)
