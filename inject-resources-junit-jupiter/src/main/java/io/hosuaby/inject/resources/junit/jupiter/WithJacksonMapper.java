package io.hosuaby.inject.resources.junit.jupiter;

import io.hosuaby.inject.resources.annotations.Parser;
import io.hosuaby.inject.resources.annotations.SupportedTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

/**
 * Annotates field or method of a test class or of a tests advice class (see {@link TestsAdvice}) that defines
 * {@link ObjectMapper} object used to parse JSON/JSONL resources.
 *
 * @see GivenJsonResource
 * @see GivenJsonLinesResource
 * @see TestsAdvice
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
@Parser
@SupportedTypes(ObjectMapper.class)
public @interface WithJacksonMapper {

    /**
     * @return Alias for {@link #name()}.
     */
    String value() default "";

    /**
     * @return Optional. A name of that parser. If it is present, parser is called 'named', or 'anonymous' if it
     * is absent.
     */
    String name() default "";
}
