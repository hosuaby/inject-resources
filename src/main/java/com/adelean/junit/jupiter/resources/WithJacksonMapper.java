package com.adelean.junit.jupiter.resources;

import com.adelean.junit.jupiter.resources.common.SupportedTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.6")
@SupportedTypes(ObjectMapper.class)
public @interface WithJacksonMapper {
    String value() default "";
    String name() default "";
}
