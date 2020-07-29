package com.adelean.inject.resources.spring.yaml;

import static com.adelean.inject.resources.commons.ClassSupport.elementType;
import static com.adelean.inject.resources.core.InjectResources.resource;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.stream.StreamSupport;

import org.yaml.snakeyaml.Yaml;
import org.springframework.context.ApplicationContext;
import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.commons.ClassSupport;
import com.adelean.inject.resources.commons.CollectionFactory;
import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.core.helpers.StringUtils;
import com.adelean.inject.resources.spring.YamlDocumentsResource;
import com.adelean.inject.resources.spring.core.AbstractResourceInjectedElement;
import com.adelean.inject.resources.spring.core.Asserts;

public final class YamlDocumentsResourceInjectElement extends AbstractResourceInjectedElement<YamlDocumentsResource> {
    public YamlDocumentsResourceInjectElement(
            Member member,
            YamlDocumentsResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    protected void assertValidField(Field field) {
        super.assertValidField(field);
        Asserts.assertArrayOrCollection("field", field.getGenericType(), YamlDocumentsResource.class);
    }

    @Override
    protected void assertValidMethod(Method method) {
        super.assertValidMethod(method);
        Parameter parameter = method.getParameters()[0];
        Asserts.assertArrayOrCollection("parameter", parameter.getParameterizedType(), YamlDocumentsResource.class);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object valueToInject(Type valueType, YamlDocumentsResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        Yaml yaml = StringUtils.isNotBlank(resourceAnnotation.yamlBean())
                ? applicationContext.getBean(resourceAnnotation.yamlBean(), Yaml.class)
                : applicationContext.getBean(Yaml.class);

        Class<?> elementType = ClassSupport.fromType(ClassSupport.elementType(valueType));

        try (ResourceAsReader resource = resource().withPath(path).asReader(charset)) {
            Iterable<Object> documents = yaml.loadAll(resource.reader());

            if (ClassSupport.isArray(valueType)) {
                return StreamSupport
                        .stream(documents.spliterator(), false)
                        .toArray(length -> (Object[]) Array.newInstance(elementType, length));
            } else if (ClassSupport.isCollection(valueType)) {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) valueType);
                documents.forEach(collection::add);
                return collection;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }
}
