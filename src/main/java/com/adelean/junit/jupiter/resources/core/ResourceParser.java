package com.adelean.junit.jupiter.resources.core;

import java.lang.reflect.Type;

public interface ResourceParser<I extends Parsable<?>, O> {
    O parse(I parsable, Type valueType);
}
