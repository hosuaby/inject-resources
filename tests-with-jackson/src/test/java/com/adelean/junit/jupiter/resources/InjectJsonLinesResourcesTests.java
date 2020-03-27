package com.adelean.junit.jupiter.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenJsonLinesResource")
public class InjectJsonLinesResourcesTests {

    @WithJacksonMapper
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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
        LocalDateTime timestamp;
        LogSeverity severity;
        String message;

        public Log(
                @JsonProperty("date")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime timestamp,
                @JsonProperty("severity")
                LogSeverity severity,
                @JsonProperty("msg")
                String message) {
            this.timestamp = timestamp;
            this.severity = severity;
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
}
