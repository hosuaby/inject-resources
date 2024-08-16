package io.hosuaby.inject.resources.spring.properties;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.spring.PropertiesResource;
import io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Properties;

public final class PropertiesResourceInjectedElement extends AbstractResourceInjectedElement<PropertiesResource> {
    public PropertiesResourceInjectedElement(
            Member member,
            PropertiesResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    public Object valueToInject(Type valueType, PropertiesResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        Properties properties = new Properties();
        resource()
                .withPath(path)
                .asReader(charset)
                .thenChecked(properties::load);

        return properties;
    }
}
