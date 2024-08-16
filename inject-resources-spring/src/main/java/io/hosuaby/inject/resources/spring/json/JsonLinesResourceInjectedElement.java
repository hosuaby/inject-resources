package io.hosuaby.inject.resources.spring.json;

import static io.hosuaby.inject.resources.commons.ClassSupport.elementType;
import static io.hosuaby.inject.resources.commons.ClassSupport.isArray;
import static io.hosuaby.inject.resources.commons.ClassSupport.isCollection;
import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.commons.CollectionFactory;
import io.hosuaby.inject.resources.core.ResourceAsLines;
import io.hosuaby.inject.resources.core.function.ThrowingFunction;
import io.hosuaby.inject.resources.core.helpers.StringUtils;
import io.hosuaby.inject.resources.parsers.Parsers;
import io.hosuaby.inject.resources.spring.JsonLinesResource;
import io.hosuaby.inject.resources.spring.core.Asserts;
import org.springframework.context.ApplicationContext;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.stream.Stream;

public final class JsonLinesResourceInjectedElement extends AbstractJsonResourceInjectedElement<JsonLinesResource> {
    public JsonLinesResourceInjectedElement(
            Member member,
            JsonLinesResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    protected void assertValidField(Field field) {
        super.assertValidField(field);
        Asserts.assertArrayOrCollection("field", field.getGenericType(), JsonLinesResource.class);
    }

    @Override
    protected void assertValidMethod(Method method) {
        super.assertValidMethod(method);
        Parameter parameter = method.getParameters()[0];
        Asserts.assertArrayOrCollection("parameter", parameter.getParameterizedType(), JsonLinesResource.class);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object valueToInject(Type valueType, JsonLinesResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());
        String parserBeanName = StringUtils.blankToNull(resourceAnnotation.parserBean());

        Type elementType = elementType(valueType);

        Object parser = findParser(parserBeanName);
        ThrowingFunction<Reader, ?> parseFunction = Parsers.parseFunction(parser, elementType);

        try (ResourceAsLines resource = resource().withPath(path).asLines(charset)) {
            Stream<Object> items = resource.parseLinesChecked(line -> {
                StringReader lineReader = new StringReader(line);
                return parseFunction.apply(lineReader);
            });

            if (isArray(valueType)) {
                return items.toArray(length -> (Object[]) Array.newInstance((Class<?>) elementType, length));
            } else if (isCollection(valueType))  {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) valueType);
                items.forEach(collection::add);
                return collection;
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }
}
