package com.adelean.inject.resources.spring.text;

import static com.adelean.inject.resources.core.InjectResources.resource;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.spring.TextResource;
import com.adelean.inject.resources.spring.core.AbstractResourceInjectedElement;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public final class TextResourceInjectedElement extends AbstractResourceInjectedElement<TextResource> {
    public TextResourceInjectedElement(
            Member member,
            TextResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    public Object valueToInject(Type valueType, TextResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .withPath(path)
                .text(charset);
    }
}
