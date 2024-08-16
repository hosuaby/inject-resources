package io.hosuaby.inject.resources.spring;

import static org.assertj.core.api.Assertions.assertThat;

import io.hosuaby.inject.resources.spring.beans.BeanWithJsonLinesResources;
import io.hosuaby.inject.resources.spring.beans.TestConfig;
import io.hosuaby.resources.data.LogSeverity;
import io.hosuaby.resources.data.jackson.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("@JsonLinesResource")
public class JsonLinesResourceAnnotationTests {

    @Autowired
    private BeanWithJsonLinesResources beanWithJsonLinesResources;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithJsonLinesResources)
                .isNotNull();
    }

    @Test
    @DisplayName("injects JSONL content into array field")
    public void testInjectJsonLinesIntoArrayField() {
        assertThat(beanWithJsonLinesResources.getLogsAsArray())
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
    public void testInjectJsonLinesIntoCollectionField() {
        assertThat(beanWithJsonLinesResources.getLogsAsCollection())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 0, 1), LogSeverity.ERROR, "Foo failed"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 4, 2), LogSeverity.INFO, "Bar was successful"),
                        new Log(LocalDateTime.of(2012, 1, 1, 2, 10, 12), LogSeverity.DEBUG, "Baz was notified"));
    }
}
