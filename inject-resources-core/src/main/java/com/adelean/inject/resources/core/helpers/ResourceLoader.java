package com.adelean.inject.resources.core.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Objects;

public final class ResourceLoader {
    private static final String ERR_RESOURCE_MISSING = "Resource '%s' is missing.";

    private final ClassLoader classLoader;

    public ResourceLoader(Class<?> clazz) {
        this(clazz.getClassLoader());
    }

    public ResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String readAsText(String resourcePath, Charset charset) {
        StringBuilder textBuilder = new StringBuilder();

        try (Reader reader = resourceReader(resourcePath, charset)) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException readResourceException) {
            throw new RuntimeException(readResourceException);
        }

        return textBuilder.toString();
    }

    public BufferedReader resourceReader(String resourcePath, Charset charset) {
        InputStream resourceStream = resourceStream(resourcePath);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceStream, charset);
        return new BufferedReader(inputStreamReader);
    }

    public byte[] readAsByteArray(String resourcePath) {
        try (InputStream is = resourceStream(resourcePath)) {
            byte[] data = new byte[is.available()];
            is.read(data);
            return data;
        } catch (IOException readResourceException) {
            throw new RuntimeException(readResourceException);
        }
    }

    public InputStream resourceStream(String resourcePath) {
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        InputStream resourceAsStream = classLoader.getResourceAsStream(resourcePath);
        return Objects.requireNonNull(resourceAsStream, String.format(ERR_RESOURCE_MISSING, resourcePath));
    }
}
