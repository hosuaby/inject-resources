package io.hosuaby.junit.jupiter.resources;

import io.hosuaby.inject.resources.junit.jupiter.TestsAdvice;
import io.hosuaby.inject.resources.junit.jupiter.WithJacksonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@TestsAdvice
public final class GlobalJacksonMapper {

    @WithJacksonMapper
    ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    @WithJacksonMapper("log-parser")
    ObjectMapper logParser() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
