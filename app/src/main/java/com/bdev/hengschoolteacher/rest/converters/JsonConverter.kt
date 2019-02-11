package com.bdev.hengschoolteacher.rest.converters

import org.codehaus.jackson.map.ObjectMapper
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter

import org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES

class JsonConverter : MappingJacksonHttpMessageConverter() {
    init {
        val objectMapper = ObjectMapper()

        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

        setObjectMapper(objectMapper)
    }
}
