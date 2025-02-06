package org.fastcampus.applicationclient.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class StoreMetered(
    val version: String = "v1",
)
