package com.adelean.inject.resources.spring.yaml;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.yaml.snakeyaml.Yaml;
import org.springframework.context.ApplicationContext;
import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.commons.ClassSupport;
import com.adelean.inject.resources.core.helpers.StringUtils;
import com.adelean.inject.resources.spring.YamlResource;
import com.adelean.inject.resources.spring.core.AbstractResourceInjectedElement;

public class YamlResourceInjectedElement extends AbstractResourceInjectedElement<YamlResource> {
    public YamlResourceInjectedElement(
            Member member,
            YamlResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    public Object valueToInject(Type valueType, YamlResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        Yaml yaml = StringUtils.isNotBlank(resourceAnnotation.yamlBean())
                ? applicationContext.getBean(resourceAnnotation.yamlBean(), Yaml.class)
                : applicationContext.getBean(Yaml.class);

        Class<?> targetClass = ClassSupport.fromType(valueType);

        return resource()
                .withPath(path)
                .asReader(charset)
                .parseChecked(reader -> yaml.loadAs(reader, targetClass));
    }
}
