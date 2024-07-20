package com.adelean.junit.jupiter.resources;

import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.adelean.resources.data.Person;
import com.adelean.resources.data.jackson.Log;
import com.adelean.resources.data.LogSeverity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@TestWithResources
@DisplayName("@TestsAdvice")
public class UseTestsAdviceTests {

    @GivenJsonLinesResource(value = "/com/adelean/junit/jupiter/logs.jsonl", jacksonMapper = "log-parser")
    static Collection<Log> logsAsCollection;

    @Test
    @DisplayName("provides default parser for tests")
    public void testTestsAdviceProvidesDefaultParser(
            @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
            Person spongeBob) {
        assertThat(spongeBob)
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstName", "Bob")
                .hasFieldOrPropertyWithValue("lastName", "Square Pants")
                .hasFieldOrPropertyWithValue("email", "sponge.bob@bikinibottom.io")
                .hasFieldOrPropertyWithValue("age", 22)
                .extracting("address", as(type(Person.Address.class)))
                .isNotNull()
                .hasFieldOrPropertyWithValue("address1", "ananas house")
                .hasFieldOrPropertyWithValue("city", "Bikini Bottom")
                .hasFieldOrPropertyWithValue("zipcode", 10101);
    }

    @Test
    @DisplayName("provides named parser for tests")
    public void testTestsAdviceProvidesNamedParser() {
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
