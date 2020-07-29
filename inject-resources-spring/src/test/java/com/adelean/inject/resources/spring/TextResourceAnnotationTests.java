package com.adelean.inject.resources.spring;

import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.spring.beans.BeanWithTextResource;
import com.adelean.inject.resources.spring.beans.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("@TextResource")
public class TextResourceAnnotationTests {

    @Autowired
    private BeanWithTextResource beanWithTextResource;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithTextResource)
                .isNotNull();
    }

    @Test
    @DisplayName("injects text content into String instance field")
    public void testInjectBinaryContentIntoByteArrayClassField() {
        assertThat(beanWithTextResource.getText())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
