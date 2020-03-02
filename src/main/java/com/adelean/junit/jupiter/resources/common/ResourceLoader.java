package com.adelean.junit.jupiter.resources.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class ResourceLoader {
    private final Class<?> testClass;

    public ResourceLoader(Class<?> testClass) {
        this.testClass = testClass;
    }

    public String readAsText(String resourcePath, Charset charset) {
        StringBuilder textBuilder = new StringBuilder();

        try (Reader reader = resourceReader(resourcePath, charset);
             BufferedReader br = new BufferedReader(reader)) {
            int c;
            while ((c = br.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException readResourceException) {
            throw new RuntimeException(readResourceException);
        }

        return textBuilder.toString();
    }

    public Reader resourceReader(String resourcePath, Charset charset) {
        InputStream resourceStream = resourceStream(resourcePath);
        return new InputStreamReader(resourceStream, charset);
    }

    public InputStream resourceStream(String resourcePath) {
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }
        return testClass.getClassLoader().getResourceAsStream(resourcePath);
    }
}
