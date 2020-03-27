package com.adelean.junitjupiter.resources;

import com.adelean.junit.jupiter.resources.GivenJsonLinesResource;
import com.adelean.junit.jupiter.resources.TestWithResources;
import com.adelean.junit.jupiter.resources.WithGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenJsonLinesResource")
public class InjectJsonLinesResourcesTests {

    @WithGson
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Log[] logsAsArray;

    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Collection<Log> logsAsCollection;

    @Test
    @DisplayName("injects JSONL content into array field")
    public void testInjectJsonLinesIntoArray() {
        assertThat(logsAsArray)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }

    @Test
    @DisplayName("injects JSONL content into collection field")
    public void testInjectJsonLinesIntoCollection() {
        assertThat(logsAsCollection)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }

    enum LogSeverity {
        DEBUG, INFO, ERROR
    }

    static class Log {

        @SerializedName("date")
        LocalDateTime timestamp;

        @SerializedName("severity")
        LogSeverity severity;

        @SerializedName("msg")
        String message;

        public Log(LocalDateTime timestamp, LogSeverity severity, String message) {
            this.timestamp = timestamp;
            this.severity = severity;
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public LogSeverity getSeverity() {
            return severity;
        }

        public void setSeverity(LogSeverity severity) {
            this.severity = severity;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            Log another = (Log) other;
            return Objects.equals(timestamp, another.timestamp)
                    && severity == another.severity
                    && Objects.equals(message, another.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, severity, message);
        }
    }

    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            var str = json.getAsJsonPrimitive().getAsString();
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
