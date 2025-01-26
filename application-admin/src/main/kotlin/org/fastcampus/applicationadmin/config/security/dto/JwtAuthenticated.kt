package org.fastcampus.applicationadmin.config.security.dto

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ROLE_CEO')")
annotation class JwtAuthenticated
