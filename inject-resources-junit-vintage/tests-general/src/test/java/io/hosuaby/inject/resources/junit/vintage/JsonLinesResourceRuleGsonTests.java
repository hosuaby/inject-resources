package io.hosuaby.inject.resources.junit.vintage;

import io.hosuaby.inject.resources.junit.vintage.json.JsonLinesResource;
import io.hosuaby.resources.data.LogSeverity;
import io.hosuaby.resources.data.gson.LocalDateTimeDeserializer;
import io.hosuaby.resources.data.gson.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonLinesResourceRuleGsonTests {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    @Rule
    public JsonLinesResource<Log[]> logsAsArray = givenResource()
            .jsonLines("/io/hosuaby/logs.jsonl")
            .parseWith(gson);

    @Rule
    public JsonLinesResource<Collection<Log>> logsAsCollection = givenResource()
            .jsonLines("/io/hosuaby/logs.jsonl")
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
