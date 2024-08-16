package io.hosuaby.inject.resources.spring.binary;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.spring.BinaryResource;
import io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

public final class BinaryResourceInjectedElement extends AbstractResourceInjectedElement<BinaryResource> {
    public BinaryResourceInjectedElement(
            Member member,
            BinaryResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    public Object valueToInject(Type valueType, BinaryResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);

        return resource()
                .withPath(path)
                .asByteArray()
                .bytes();
    }
}
