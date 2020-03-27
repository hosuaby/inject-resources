package com.adelean.junit.jupiter.resources.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.adelean.junit.jupiter.resources.core.annotations.Extends;
import com.adelean.junit.jupiter.resources.core.annotations.Named;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Extends(Named.class)
public @interface AnnotationWithoutName {
}
