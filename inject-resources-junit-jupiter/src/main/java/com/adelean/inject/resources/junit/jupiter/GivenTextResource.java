package com.adelean.inject.resources.junit.jupiter;

import com.adelean.inject.resources.junit.jupiter.core.annotations.Extends;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Resource;
import com.adelean.inject.resources.junit.jupiter.core.annotations.SupportedTypes;
import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

/**
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.6")
@Resource
@Extends(GivenBinaryResource.class)
@SupportedTypes(String.class)
public @interface GivenTextResource {
    String value() default "";
    String from() default "";
    String charset() default "UTF-8";
}
