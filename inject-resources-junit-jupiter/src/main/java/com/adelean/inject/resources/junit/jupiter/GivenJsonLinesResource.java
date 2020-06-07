package com.adelean.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Extends;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Resource;

/**
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.6")
@Resource
@Extends(GivenJsonResource.class)
public @interface GivenJsonLinesResource {
    String value() default "";
    String from() default "";
    String charset() default "UTF-8";
    String jacksonMapper() default "";
    String gson() default "";
}
