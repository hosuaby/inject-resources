package com.adelean.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates public final class that becomes tests advice. Tests advice defines parsers available in every test class
 * (those parsers can be considered to be "global" parsers).
 *
 * <p>Only one class annotated with {@code TestsAdvice} is allowed on classpath.</p>
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;TestsAdvice
 * public final class GlobalJacksonMapper {
 *
 *     &#64;WithJacksonMapper("custom-mapper")
 *     ObjectMapper objectMapper() {
 *         return new ObjectMapper().registerModule(new JavaTimeModule());
 *     }
 * }
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
public @interface TestsAdvice {
}
