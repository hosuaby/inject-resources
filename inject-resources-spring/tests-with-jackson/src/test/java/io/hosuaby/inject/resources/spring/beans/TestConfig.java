package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.EnableResourceInjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableResourceInjection
@ComponentScan(basePackageClasses = TestConfig.class)
public class TestConfig {

    @Bean
    @Primary
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectMapper logsObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }
}
