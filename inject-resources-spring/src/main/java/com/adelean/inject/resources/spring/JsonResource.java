package com.adelean.inject.resources.spring;

import com.adelean.inject.resources.annotations.Extends;
import com.adelean.inject.resources.annotations.Resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates beans field, constructor argument or setter method that must be injected with parsed content of JSON
 * resource file with path {@code 'from'}.
 *
 * <p>JSON is parsed using Jackson {@code ObjectMapper} or {@code Gson} present in application context.
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;JsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", parserBean = "defaultObjectMapper")
 * private Person spongeBob;
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Resource
@Extends(TextResource.class)
public @interface JsonResource {

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
     * @return name of {@code ObjectMapper} or {@code Gson} bean present in application context. If {@code parserBean}
     * is an empty string, the primary parser object will be used.
     */
    String parserBean() default "";
}
