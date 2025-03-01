package org.fastcampus.payment.gateway.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.optionals.OptionalDecoder
import org.fastcampus.payment.gateway.error.Pay200ErrorDecoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class Pay200ClientConfig {
    @Bean
    fun requestInterceptor(
        @Value("\${external.pay200.secret}") secret: String,
    ): RequestInterceptor {
        // 인증헤더 설정
        return RequestInterceptor { requestTemplate ->
            requestTemplate.header("Authorization", "Basic $secret")
        }
    }

    @Bean
    fun tossPaymentsErrorDecoder(): Pay200ErrorDecoder {
        return Pay200ErrorDecoder(feignObjectMapper())
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
        objectMapper.apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerKotlinModule()
        }
        return objectMapper
    }
}
