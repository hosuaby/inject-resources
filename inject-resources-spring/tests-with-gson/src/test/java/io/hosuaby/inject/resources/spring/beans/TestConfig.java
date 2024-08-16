package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.EnableResourceInjection;
import io.hosuaby.resources.data.gson.LocalDateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

@Configuration
@EnableResourceInjection
@ComponentScan(basePackageClasses = TestConfig.class)
public class TestConfig {

    @Bean
    @Primary
    public Gson defaultGson() {
        return new Gson();
    }

    @Bean
    public Gson logsGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
    }
}
