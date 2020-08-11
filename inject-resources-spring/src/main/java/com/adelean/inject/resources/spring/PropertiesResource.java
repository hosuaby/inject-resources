package com.adelean.inject.resources.spring;

import com.adelean.inject.resources.annotations.Extends;
import com.adelean.inject.resources.annotations.Resource;
import com.adelean.inject.resources.annotations.SupportedTypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

/**
 * Annotates beans field, constructor argument or setter method that must be injected with content of properties
 * resource file with path {@code 'from'}.
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;PropertiesResource("/com/adelean/junit/jupiter/db.properties")
 * private Properties dbProperties;
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resource
@Extends(TextResource.class)
@SupportedTypes(Properties.class)
public @interface PropertiesResource {

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
