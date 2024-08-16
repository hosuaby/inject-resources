package io.hosuaby.inject.resources.junit.jupiter.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.hosuaby.inject.resources.annotations.Extends;
import io.hosuaby.inject.resources.annotations.Named;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Extends(Named.class)
public @interface AnnotationWithoutName {
}
