package com.adelean.inject.resources.core;

import static com.adelean.inject.resources.core.InjectResources.resource;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;

public class JavaDslTests {
    private static final JavaPropsSchema SCHEMA = JavaPropsSchema
            .emptySchema()
            .withoutPathSeparator();
    private static final ObjectReader READER = new JavaPropsMapper()
            .readerFor(DbConnection.class)
            .with(SCHEMA);
    private static final String PATH_PREFIX = "/com/adelean/junit/jupiter";

    @Test
    @DisplayName("Test resource as binary")
    public void testResourceAsBinary() {
        byte[] fibonacci = resource()
                .withPath(PATH_PREFIX, "fibonacci.bin")
                .asByteArray()
                .bytes();

        assertThat(fibonacci)
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("Test resource as text")
    public void testResourceAsText() {
        var text = resource()
                .withPath(PATH_PREFIX, "resource.txt")
                .asText()
                .text();

        assertThat(text)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }

    @Test
    @DisplayName("Test resource as stream")
    public void testResourceAsStream() {
        var dbConnection = resource()
                .withPath(PATH_PREFIX, "db.properties")
                .asInputStream()
                .parseChecked(READER::readValue);

        assertThat(dbConnection)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user", "hosuaby")
                .hasFieldOrPropertyWithValue("password", "password")
                .hasFieldOrPropertyWithValue("url", "localhost");
    }

    @Test
    @DisplayName("Test resource as lines")
    public void testResourceAsLines() {
        var header = new AtomicReference<>();
        var lines = new ArrayList<String>();

        resource()
                .onClassLoaderOf(JavaDslTests.class)
                .withPath(PATH_PREFIX, "cities.csv")
                .asLines()
                .onFirstLine(header::set)
                .forEachLine(lines::add);

        assertThat((String) header.get())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .startsWith("\"LatD\", \"LatM\",");

        assertThat(lines)
                .isNotEmpty()
                .hasSize(128);
    }

    static class DbConnection {
        @JsonProperty("db.user") String user;
        @JsonProperty("db.password") String password;
        @JsonProperty("db.url") String url;
    }
}
