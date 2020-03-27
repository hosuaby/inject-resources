package com.adelean.junit.jupiter.resources;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import com.adelean.junit.jupiter.resources.core.annotations.Parser;
import com.adelean.junit.jupiter.resources.core.annotations.SupportedTypes;
import com.google.gson.Gson;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.6")
@Parser
@SupportedTypes(Gson.class)
public @interface WithGson {
    String value() default "";
    String name() default "";
}
