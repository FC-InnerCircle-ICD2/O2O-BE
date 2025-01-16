package org.fastcampus.applicationclient.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SwaggerConfigTest(
    @Autowired
    private val mockMvc: MockMvc,
) {
    @Test
    fun swaggerConfigTest() {
        mockMvc.get("/swagger-ui.html")
            .andExpect {
                status().isOk // 200 상태 코드 확인
            }
    }
}
