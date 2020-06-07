package com.adelean.inject.resources.core;

public final class InjectResources {
    private InjectResources() {
    }

    public static InjectResources resource() {
        return new InjectResources();
    }

    public Resource.ResourceOnClassloader onClassLoaderOf(Class<?> clazz) {
        return onClassLoader(clazz.getClassLoader());
    }

    public Resource.ResourceOnClassloader onClassLoader(ClassLoader classLoader) {
        return new Resource.ResourceOnClassloader(classLoader);
    }

    public Resource withPath(String firstPathToken, String... otherTokens) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return onClassLoader(contextClassLoader).withPath(firstPathToken, otherTokens);
    }
}
