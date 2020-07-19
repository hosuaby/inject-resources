package com.adelean.inject.resources.junit.vintage.core;

import org.junit.rules.TestRule;

public interface ResourceRule<T> extends TestRule {
    T get();
}
