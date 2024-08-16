package io.hosuaby.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import io.hosuaby.inject.resources.annotations.Parser;
import io.hosuaby.inject.resources.annotations.SupportedTypes;
import com.google.gson.Gson;

/**
 * Annotates field or method of a test class or of a tests advice class (see {@link TestsAdvice}) that defines
 * {@link Gson} object used to parse JSON/JSONL resources.
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
@SupportedTypes(Gson.class)
public @interface WithGson {

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
