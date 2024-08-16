package io.hosuaby.inject.resources.spring;

import io.hosuaby.inject.resources.annotations.Resource;
import io.hosuaby.inject.resources.annotations.SupportedTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates beans field, constructor argument or setter method that must be injected with content of binary resource
 * file with path {@code 'from'}.
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;BinaryResource("/io/hosuaby/fibonacci.bin")
 * private byte[] fibonacciInstanceField;
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resource
@SupportedTypes(byte[].class)
public @interface BinaryResource {

    /**
     * @return Alias for {@link #from()}.
     */
    String value() default "";

    /**
     * @return Absolute path to requested binary resource file.
     */
    String from() default "";
}
