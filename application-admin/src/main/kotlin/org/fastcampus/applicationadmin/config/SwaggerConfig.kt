package org.fastcampus.applicationadmin.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .version("v0.0.1")
                    .title("개발의 민족 점주 API Specification")
                    .description("개발의 민족 서비스의 점주 측 API 명세서 입니다."),
            )
    }
}
