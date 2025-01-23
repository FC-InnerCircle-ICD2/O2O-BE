package org.fastcampus.applicationadmin.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@PropertySource("classpath:/env.properties")
@Configuration
class EnvironmentConfig(
    private val environment: Environment,
) {
    fun getProperty(key: String): String? {
        return environment.getProperty(key)
    }
}
