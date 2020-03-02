package com.adelean.junit.jupiter.resources.text;

import com.adelean.junit.jupiter.resources.GivenTextResource;
import com.adelean.junit.jupiter.resources.common.ResourcesInjector;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public final class TextResourcesInjector extends ResourcesInjector<GivenTextResource> {
    public TextResourcesInjector(
            ExtensionContext context,
            Object testInstance,
            Class<?> testClass) {
        super(context, testInstance, testClass, GivenTextResource.class);
    }

    @Override
    public Object valueToInject(Type valueType, GivenTextResource resourceAnnotation) {
        String path = StringUtils.isNotBlank(resourceAnnotation.from())
                ? resourceAnnotation.from()
                : resourceAnnotation.value();
        Charset charset = Charset.forName(resourceAnnotation.charset());
        return resourceLoader.readAsText(path, charset);
    }
}
