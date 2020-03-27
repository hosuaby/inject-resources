package com.adelean.junit.jupiter.resources;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

import org.apiguardian.api.API;

import com.adelean.junit.jupiter.resources.core.annotations.Extends;
import com.adelean.junit.jupiter.resources.core.annotations.Resource;
import com.adelean.junit.jupiter.resources.core.annotations.SupportedTypes;

/**
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.6")
@Resource
@Extends(GivenTextResource.class)
@SupportedTypes(Properties.class)
public @interface GivenPropertiesResource {
    String value() default "";
    String from() default "";
    String charset() default "UTF-8";
}
