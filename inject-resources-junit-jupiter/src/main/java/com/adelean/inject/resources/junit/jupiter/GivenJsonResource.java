package com.adelean.inject.resources.junit.jupiter;

import org.apiguardian.api.API;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import com.adelean.inject.resources.annotations.Extends;
import com.adelean.inject.resources.annotations.Resource;

/**
 * Annotates field or test parameter that must be injected with parsed content of JSON resource file with path
 * {@code 'from'}.
 *
 * @see WithJacksonMapper
 * @see WithGson
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
@Resource
@Extends(GivenTextResource.class)
public @interface GivenJsonResource {

    /**
     * @return Alias for {@link #from()}.
     */
    String value() default "";

    /**
     * @return Absolute path to requested JSON resource file.
     */
    String from() default "";

    /**
     * @return Encoding charset of resource file.
     */
    String charset() default "UTF-8";

    /**
     * @return Optional. Name of Jackson {@code ObjectMapper} object annotated with {@link WithJacksonMapper} that will
     * be used to parse resource.
     */
    String jacksonMapper() default "";

    /**
     * @return Optional. Name of GSON {@code Gson} object annotated with {@link WithGson} that will be used to parse
     * resource.
     */
    String gson() default "";
}
