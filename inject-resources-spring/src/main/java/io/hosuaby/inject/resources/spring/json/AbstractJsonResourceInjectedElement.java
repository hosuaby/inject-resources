package io.hosuaby.inject.resources.spring.json;

import static io.hosuaby.inject.resources.commons.ClasspathSupport.GSON_CLASS_NAME;
import static io.hosuaby.inject.resources.commons.ClasspathSupport.JACKSON_MAPPER_CLASS_NAME;
import static io.hosuaby.inject.resources.commons.ClasspathSupport.isGsonPresent;
import static io.hosuaby.inject.resources.commons.ClasspathSupport.isJackson2Present;

import io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;

abstract class AbstractJsonResourceInjectedElement<A extends Annotation>
        extends AbstractResourceInjectedElement<A> {
    private static final String ERR_NO_PARSER_BEAN =
            "No JSON parser bean was found.\nBean of any of following types is required:"
                    + "\n\t- " + JACKSON_MAPPER_CLASS_NAME
                    + "\n\t- " + GSON_CLASS_NAME;

    protected AbstractJsonResourceInjectedElement(
            Member member,
            A resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    protected Object findParser(@Nullable String parserBeanName) {
        Object parser = null;

        if (parserBeanName != null) {
            parser = applicationContext.getBean(parserBeanName);
        } else {
            if (isJackson2Present()) {
                Class<?> jacksonMapperClass = AbstractResourceInjectedElement.REFLECTIONS
                        .forClass(JACKSON_MAPPER_CLASS_NAME);
                try {
                    parser = applicationContext.getBean(jacksonMapperClass);
                } catch (Exception ignored) {
                }
            }

            if (parser == null && isGsonPresent()) {
                Class<?> gsonClass = AbstractResourceInjectedElement.REFLECTIONS.forClass(GSON_CLASS_NAME);
                try {
                    parser = applicationContext.getBean(gsonClass);
                } catch (Exception ignored) {
                }
            }
        }

        if (parser != null) {
            return parser;
        } else {
            throw new NoSuchBeanDefinitionException("", ERR_NO_PARSER_BEAN);
        }
    }
}
