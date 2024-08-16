package io.hosuaby.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import io.hosuaby.inject.resources.annotations.Extends;
import io.hosuaby.inject.resources.annotations.Resource;

/**
 * Annotates field or test parameter that must be injected with parsed content of YAML resource file containing multiple
 * documents (separated by three hyphens '---') with path {@code 'from'}.
 *
 * @see WithSnakeYaml
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
@Resource
@Extends(GivenYamlResource.class)
public @interface GivenYamlDocumentsResource {

    /**
     * @return Alias for {@link #from()}.
     */
    String value() default "";

    /**
     * @return Absolute path to requested YAML resource file (with multiple documents).
     */
    String from() default "";

    /**
     * @return Encoding charset of resource file.
     */
    String charset() default "UTF-8";

    /**
     * @return Optional. Name of Snakeyaml {@code Yaml} object annotated with {@link WithSnakeYaml} that will be used to
     * parse resource.
     */
    String yaml() default "";
}
