package io.hosuaby.inject.resources.spring;

import io.hosuaby.inject.resources.spring.beans.ResourceInjectionConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables resource content injection.
 *
 * <p>Usage:</p>
 *
 * <pre>
 * &#64;Configuration
 * &#64;EnableResourceInjection
 * public class MyConfig {
 * }
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ResourceInjectionConfiguration.class)
@Documented
public @interface EnableResourceInjection {
}
