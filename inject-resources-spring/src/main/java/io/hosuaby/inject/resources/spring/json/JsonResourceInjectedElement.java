package io.hosuaby.inject.resources.spring.json;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.function.ThrowingFunction;
import io.hosuaby.inject.resources.core.helpers.StringUtils;
import io.hosuaby.inject.resources.parsers.Parsers;
import io.hosuaby.inject.resources.spring.JsonResource;
import org.springframework.context.ApplicationContext;

import java.io.Reader;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

public final class JsonResourceInjectedElement extends AbstractJsonResourceInjectedElement<JsonResource> {
    public JsonResourceInjectedElement(
            Member member,
            JsonResource resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, resourceAnnotation, applicationContext);
    }

    @Override
    public Object valueToInject(Type valueType, JsonResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());
        String parserBeanName = StringUtils.blankToNull(resourceAnnotation.parserBean());

        Object parser = findParser(parserBeanName);
        ThrowingFunction<Reader, ?> parseFunction = Parsers.parseFunction(parser, valueType);

        return resource()
                .withPath(path)
                .asReader(charset)
                .parseChecked(parseFunction);
    }
}
