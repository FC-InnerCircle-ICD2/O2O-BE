package org.fastcampus.payment.gateway

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.optionals.OptionalDecoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.util.*

internal class TossPaymentsClientConfig {
    @Bean
    fun requestInterceptor(
        @Value("\${external.tosspayments.secret}") secret: String,
    ): RequestInterceptor {
        // 인증헤더 설정
        val encodedSecretKey = Base64.getEncoder().encodeToString("$secret:".toByteArray())
        return RequestInterceptor { requestTemplate ->
            requestTemplate.header("Authorization", "Basic $encodedSecretKey")
        }
    }

    @Bean
    fun tossPaymentsErrorDecoder(objectMapper: ObjectMapper): TossPaymentsErrorDecoder {
        return TossPaymentsErrorDecoder(objectMapper)
    }

    @Bean
    fun feignDecoder(): Decoder {
        val jacksonConverter: HttpMessageConverter<*> = MappingJackson2HttpMessageConverter(feignObjectMapper())
        val objectFactory: ObjectFactory<HttpMessageConverters> =
            ObjectFactory<HttpMessageConverters> { HttpMessageConverters(jacksonConverter) }
        return OptionalDecoder(ResponseEntityDecoder(SpringDecoder(objectFactory)))
    }

    @Bean
    fun feignEncoder(): Encoder {
        val jacksonConverter: HttpMessageConverter<*> = MappingJackson2HttpMessageConverter(feignObjectMapper())
        val objectFactory: ObjectFactory<HttpMessageConverters> =
            ObjectFactory<HttpMessageConverters> { HttpMessageConverters(jacksonConverter) }
        return SpringEncoder(objectFactory)
    }

    @Bean("feignObjectMapper")
    fun feignObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }
}
