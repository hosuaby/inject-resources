package com.adelean.inject.resources.spring;

import com.adelean.inject.resources.annotations.Extends;
import com.adelean.inject.resources.annotations.Resource;
import com.adelean.inject.resources.annotations.SupportedTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

/**
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resource
@Extends(TextResource.class)
@SupportedTypes(Properties.class)
public @interface PropertiesResource {
    String value() default "";
    String from() default "";
    String charset() default "UTF-8";
}
