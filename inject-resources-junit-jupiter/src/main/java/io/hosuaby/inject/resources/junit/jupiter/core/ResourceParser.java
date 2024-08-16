package io.hosuaby.inject.resources.junit.jupiter.core;

import java.lang.reflect.Type;

import io.hosuaby.inject.resources.core.Parsable;

public interface ResourceParser<I extends Parsable<?>, O> {
    O parse(I parsable, Type valueType);
}
