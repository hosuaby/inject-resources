package com.adelean.inject.resources.junit.vintage;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.junit.vintage.json.JsonLinesResource;
import com.adelean.junit.jupiter.resources.data.LogSeverity;
import com.adelean.junit.jupiter.resources.data.gson.LocalDateTimeDeserializer;
import com.adelean.junit.jupiter.resources.data.gson.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collection;

public class JsonLinesResourceRuleGsonTests {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    @Rule
    public JsonLinesResource<Log[]> logsAsArray = givenResource()
            .jsonLines("/com/adelean/junit/jupiter/logs.jsonl")
            .parseWith(gson);

    @Rule
    public JsonLinesResource<Collection<Log>> logsAsCollection = givenResource()
            .jsonLines("/com/adelean/junit/jupiter/logs.jsonl")
            .parseWith(gson);

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
