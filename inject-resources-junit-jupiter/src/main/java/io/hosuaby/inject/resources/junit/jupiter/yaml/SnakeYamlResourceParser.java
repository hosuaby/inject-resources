package io.hosuaby.inject.resources.junit.jupiter.yaml;

import java.lang.reflect.Type;

import io.hosuaby.inject.resources.commons.ClassSupport;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceParser;
import org.yaml.snakeyaml.Yaml;
import io.hosuaby.inject.resources.core.ResourceAsReader;

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
