package com.adelean.junit.jupiter.resources;

import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.adelean.junit.jupiter.resources.data.Log;
import com.adelean.junit.jupiter.resources.data.LogSeverity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@TestsAdvice")
public class UseTestsAdviceTests {

    @Test
    @DisplayName("provides parser for tests")
    public void testTestsAdviceProvidesParser(
            @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
            Collection<Log> logsAsCollection) {
        assertThat(logsAsCollection)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }
}
