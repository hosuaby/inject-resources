package io.hosuaby.inject.resources.junit.vintage;

import io.hosuaby.inject.resources.junit.vintage.yaml.YamlDocumentsResource;
import io.hosuaby.resources.data.LogSeverity;
import io.hosuaby.resources.data.snakeyaml.Log;
import org.junit.Rule;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlDocumentsResourceRuleTests {
    private static final Yaml yaml = new Yaml();
    private static final Yaml logParser = new Yaml(new Constructor(Log.class, new LoaderOptions()));

    @Rule
    public YamlDocumentsResource<List<Map<String, Object>>> stacktraceAsList = givenResource()
            .yamlDocuments("/io/hosuaby/stacktrace.yaml")
            .parseWith(yaml);

    @Rule
    public YamlDocumentsResource<Map<String, Object>[]> stacktraceAsArray = givenResource()
            .yamlDocuments("/io/hosuaby/stacktrace.yaml")
            .parseWith(yaml);

    @Rule
    public YamlDocumentsResource<Log[]> logsAsArray = givenResource()
            .yamlDocuments("/io/hosuaby/logs.yml")
            .parseWith(logParser);

    @Rule
    public YamlDocumentsResource<Collection<Log>> logsAsCollection = givenResource()
            .yamlDocuments("/io/hosuaby/logs.yml")
            .parseWith(logParser);

    @Test
    public void testLoadYamlDocumentsIntoList() {
        List<Map<String, Object>> actual = stacktraceAsList.get();
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        assertThat(actual.get(0))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 1, 42)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "This is an error message for the log file");

        assertThat(actual.get(1))
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 2, 31)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "A slightly different error message.");

        assertThat(actual.get(2))
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
        assertThat(actual.get(2).get("Stack"))
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        Map.of(
                                "file", "TopClass.py",
                                "line", 23,
                                "code", "x = MoreObject(\"345\\n\")\n"),
                        Map.of(
                                "file", "MoreClass.py",
                                "line", 58,
                                "code", "foo = bar"));
    }

    @Test
    public void testLoadYamlDocumentsIntoArrayOfMaps() {
        Map<String, Object>[] actual = stacktraceAsArray.get();
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        assertThat(actual[0])
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 1, 42)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "This is an error message for the log file");

        assertThat(actual[1])
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("Time", Date.from(LocalDateTime
                        .of(2001, 11, 23, 15, 2, 31)
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()))
                .containsEntry("User", "ed")
                .containsEntry("Warning", "A slightly different error message.");

        assertThat(actual[2])
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
        assertThat(actual[2].get("Stack"))
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        Map.of(
                                "file", "TopClass.py",
                                "line", 23,
                                "code", "x = MoreObject(\"345\\n\")\n"),
                        Map.of(
                                "file", "MoreClass.py",
                                "line", 58,
                                "code", "foo = bar"));
    }

    @Test
    public void testLoadYamlDocumentsIntoTypedArray() {
        assertThat(logsAsArray.get())
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
    public void testLoadYamlDocumentsIntoCollection() {
        assertThat(logsAsCollection.get())
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
