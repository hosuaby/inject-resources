package com.adelean.inject.resources.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.adelean.inject.resources.spring.beans.BeanWithYamlDocumentsResources;
import com.adelean.inject.resources.spring.beans.TestConfig;
import com.adelean.resources.data.LogSeverity;
import com.adelean.resources.data.snakeyaml.Log;
import com.tngtech.archunit.thirdparty.com.google.common.collect.ImmutableMap;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("@YamlDocumentsResource")
public class YamlDocumentsResourceAnnotationTests {

    @Autowired
    private BeanWithYamlDocumentsResources beanWithYamlDocumentsResources;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithYamlDocumentsResources)
                .isNotNull();
    }

    @Test
    @DisplayName("injects content of YAML resource with multiple documents into List instance field")
    public void testInjectsYamlDocumentsIntoListField() {
        var stacktraceAsList = beanWithYamlDocumentsResources.getStacktraceAsList();
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
        var stacktraceAsArray = beanWithYamlDocumentsResources.getStacktraceAsArray();
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
        assertThat(beanWithYamlDocumentsResources.getLogsAsArray())
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
        assertThat(beanWithYamlDocumentsResources.getLogsAsCollection())
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
