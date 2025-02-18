package org.fastcampus.applicationclient.config.security.dto

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ROLE_USER')")
annotation class JwtAuthenticated
