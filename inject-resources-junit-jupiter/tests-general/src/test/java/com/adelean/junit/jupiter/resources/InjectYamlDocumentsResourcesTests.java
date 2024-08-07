package com.adelean.junit.jupiter.resources;

import com.adelean.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.adelean.inject.resources.junit.jupiter.WithSnakeYaml;
import com.adelean.resources.data.LogSeverity;
import com.adelean.resources.data.snakeyaml.Log;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenYamlDocumentsResource")
public class InjectYamlDocumentsResourcesTests {

    @WithSnakeYaml("default")
    Yaml yaml = new Yaml();

    @WithSnakeYaml("log-parser")
    Yaml logParser = new Yaml(new Constructor(Log.class, new LoaderOptions()));

    @GivenYamlDocumentsResource(from = "/com/adelean/junit/jupiter/stacktrace.yaml", yaml = "default")
    List<Map<String, Object>> stacktraceAsList;

    @GivenYamlDocumentsResource(from = "/com/adelean/junit/jupiter/stacktrace.yaml", yaml = "default")
    Map<String, Object>[] stacktraceAsArray;

    @GivenYamlDocumentsResource(from = "/com/adelean/junit/jupiter/logs.yml", yaml = "log-parser")
    Log[] logsAsArray;

    @GivenYamlDocumentsResource(from = "/com/adelean/junit/jupiter/logs.yml", yaml = "log-parser")
    Collection<Log> logsAsCollection;

    @Test
    @DisplayName("injects content of YAML resource with multiple documents into List instance field")
    public void testInjectsYamlDocumentsIntoListField() {
        assertThat(stacktraceAsList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        assertThat(stacktraceAsList.get(0))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 1, 42)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "This is an error message for the log file");

        assertThat(stacktraceAsList.get(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 2, 31)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "A slightly different error message.");

        assertThat(stacktraceAsList.get(2))
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)
                .containsEntry("Date", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 3, 17)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsKey("Stack")
                .containsEntry("User", "ed")
                .containsEntry("Fatal", "Unknown variable \"bar\"");
        assertThat(stacktraceAsList.get(2).get("Stack"))
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        ImmutableMap.of(
                                "file", "TopClass.py",
                                "line", 23,
                                "code", "x = MoreObject(\"345\\n\")\n"),
                        ImmutableMap.of(
                                "file", "MoreClass.py",
                                "line", 58,
                                "code", "foo = bar"));
    }

    @Test
    @DisplayName("injects content of YAML resource with multiple documents into array instance field")
    public void testInjectsYamlDocumentsIntoArrayField() {
        assertThat(stacktraceAsArray)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        assertThat(stacktraceAsArray[0])
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 1, 42)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "This is an error message for the log file");

        assertThat(stacktraceAsArray[1])
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 2, 31)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "A slightly different error message.");

        assertThat(stacktraceAsArray[2])
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)
                .containsKey("Stack")
                .containsEntry("Date", Date.from(LocalDateTime
                                .of(2001, 11, 23, 15, 3, 17)
                                .atZone(ZoneId.of("UTC"))
                                .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Fatal", "Unknown variable \"bar\"");
        assertThat(stacktraceAsArray[2].get("Stack"))
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        ImmutableMap.of(
                                "file", "TopClass.py",
                                "line", 23,
                                "code", "x = MoreObject(\"345\\n\")\n"),
                        ImmutableMap.of(
                                "file", "MoreClass.py",
                                "line", 58,
                                "code", "foo = bar"));
    }

    @Test
    @DisplayName("injects YAML documents content into array field")
    public void testInjectYamlDocumentsIntoArray() {
        assertThat(logsAsArray)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 0, 1).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.ERROR,
                                "Foo failed"),
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 4, 2).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.INFO,
                                "Bar was successful"),
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 10, 12).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.DEBUG,
                                "Baz was notified"));
    }

    @Test
    @DisplayName("injects YAML documents content into collection field")
    public void testInjectYamlDocumentsIntoCollectionField() {
        assertThat(logsAsCollection)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 0, 1).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.ERROR,
                                "Foo failed"),
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 4, 2).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.INFO,
                                "Bar was successful"),
                        new Log(
                                Date.from(LocalDateTime.of(2012, 1, 1, 2, 10, 12).atZone(ZoneId.of("UTC")).toInstant()),
                                LogSeverity.DEBUG,
                                "Baz was notified"));
    }
}
