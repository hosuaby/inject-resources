package com.adelean.inject.resources.junit.vintage;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.junit.vintage.json.JsonLinesResource;
import com.adelean.junit.jupiter.resources.data.LogSeverity;
import com.adelean.junit.jupiter.resources.data.jackson.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;

public class JsonLinesResourceRuleJacksonTests {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Rule
    public JsonLinesResource<Log[]> logsAsArray = givenResource()
            .jsonLines("/com/adelean/junit/jupiter/logs.jsonl")
            .withCharset(StandardCharsets.UTF_8)
            .parseWith(objectMapper);

    @Rule
    public JsonLinesResource<Collection<Log>> logsAsCollection = givenResource()
            .jsonLines("/com/adelean/junit/jupiter/logs.jsonl")
            .parseWith(objectMapper)
            .withCharset(StandardCharsets.UTF_8);

    @Test
    public void testLoadJsonLinesIntoArray() {
        assertThat(logsAsArray.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }

    @Test
    public void testLoadJsonLinesIntoCollection() {
        assertThat(logsAsCollection.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }
}
