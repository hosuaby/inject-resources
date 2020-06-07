package com.adelean.junit.jupiter.resources;

import com.adelean.inject.resources.junit.jupiter.TestsAdvice;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@TestsAdvice
public final class GlobalJacksonMapper {

    @WithJacksonMapper
    ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
