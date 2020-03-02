package com.adelean.junit.jupiter.resources.json;

import com.adelean.junit.jupiter.resources.GivenJsonResource;
import com.adelean.junit.jupiter.resources.common.InjectionContext;
import com.adelean.junit.jupiter.resources.common.ResourcesInjector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Optional;

public final class JacksonJsonResourcesInjector extends ResourcesInjector<GivenJsonResource> {
    public static final String ERR_NAMED_JACKSON_MAPPER_MISSING =
            "Can't find named ObjectMapper '%s' in execution context. You probably forgot @WithJacksonMapper(\"%s\")";

    private final InjectionContext injectionContext;

    public JacksonJsonResourcesInjector(
            ExtensionContext context,
            Object testInstance,
            Class<?> testClass) {
        super(context, testInstance, testClass, GivenJsonResource.class);
        this.injectionContext = new InjectionContext(context);
    }

    @Override
    public Object valueToInject(Type valueType, GivenJsonResource resourceAnnotation) {
        String path = StringUtils.isNotBlank(resourceAnnotation.from())
                ? resourceAnnotation.from()
                : resourceAnnotation.value();
        Charset charset = Charset.forName(resourceAnnotation.charset());

        String jacksonMapperName = StringUtils.isNotBlank(resourceAnnotation.jacksonMapper())
                ? resourceAnnotation.jacksonMapper()
                : null;

        Optional<ObjectMapper> bean = injectionContext.findBean(testClass, jacksonMapperName, ObjectMapper.class);

        if (!bean.isPresent() && jacksonMapperName != null) {
            throw new RuntimeException(String.format(
                    ERR_NAMED_JACKSON_MAPPER_MISSING, jacksonMapperName, jacksonMapperName));
        }

        ObjectMapper mapper = bean.orElseGet(ObjectMapper::new);

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(valueType);

        try (Reader resourceReader = resourceLoader.resourceReader(path, charset)) {
            return mapper.readValue(resourceReader, javaType);
        } catch (IOException jsonParsingException) {
            throw new RuntimeException(jsonParsingException);
        }
    }
}
