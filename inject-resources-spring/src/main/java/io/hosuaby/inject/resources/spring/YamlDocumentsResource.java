package io.hosuaby.inject.resources.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.hosuaby.inject.resources.annotations.Extends;
import io.hosuaby.inject.resources.annotations.Resource;

/**
 * Annotates beans field, constructor argument or setter method that must be injected with parsed content of YAML
 * resource file containing multiple documents (separated by three hyphens '---') with path {@code 'from'}.
 *
 * <p>YAML is parsed using Snakeyaml {@code Yaml} object present in application context.
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;YamlDocumentsResource(from = "/io/hosuaby/logs.yml", yamlBean = "log-parser")
 * Log[] logsAsArray;
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resource
@Extends(YamlResource.class)
public @interface YamlDocumentsResource {

    /**
     * @return Alias for {@link #from()}.
     */
    String value() default "";

    /**
     * @return Absolute path to requested YAML resource file.
     */
    String from() default "";

    /**
     * @return Encoding charset of resource file.
     */
    String charset() default "UTF-8";

    /**
     * @return name of Snakeyaml {@code Yaml} bean present in application context. If {@code yamlBean} is an empty
     * string, the primary parser object will be used.
     */
    String yamlBean() default "";
}
