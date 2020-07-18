package com.adelean.inject.resources.junit.jupiter.yaml;

import java.lang.reflect.Type;

import org.yaml.snakeyaml.Yaml;
import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.core.ResourceParser;
import com.adelean.inject.resources.junit.jupiter.core.helpers.ClassSupport;

public final class SnakeYamlResourceParser implements ResourceParser<ResourceAsReader, Object> {
    private final Yaml yaml;

    public SnakeYamlResourceParser(Yaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Object parse(ResourceAsReader resource, Type valueType) {
        return resource.parse(reader -> yaml.loadAs(reader, ClassSupport.fromType(valueType)));
    }

    public Yaml getYaml() {
        return yaml;
    }
}
