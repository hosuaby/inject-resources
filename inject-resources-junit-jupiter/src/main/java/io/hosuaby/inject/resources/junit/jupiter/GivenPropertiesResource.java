package io.hosuaby.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

import org.apiguardian.api.API;

import io.hosuaby.inject.resources.annotations.Extends;
import io.hosuaby.inject.resources.annotations.Resource;
import io.hosuaby.inject.resources.annotations.SupportedTypes;

/**
 * Annotates field or test parameter that must be injected with content of properties resource file with path
 * {@code 'from'}.
 *
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
@Resource
@Extends(GivenTextResource.class)
@SupportedTypes(Properties.class)
public @interface GivenPropertiesResource {

    /**
     * @return Alias for {@link #from()}.
     */
    String value() default "";

    /**
     * @return Absolute path to requested properties resource file.
     */
    String from() default "";

    /**
     * @return Encoding charset of resource file.
     */
    String charset() default "UTF-8";
}
