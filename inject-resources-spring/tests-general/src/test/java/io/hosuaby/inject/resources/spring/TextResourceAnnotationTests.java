package io.hosuaby.inject.resources.spring;

import io.hosuaby.inject.resources.spring.beans.BeanWithTextResource;
import io.hosuaby.inject.resources.spring.beans.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void testInjectTextContentIntoStringClassField() {
        assertThat(beanWithTextResource.getText())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }

    @Test
    @DisplayName("injects text content into String constructor argument")
    public void testInjectTextContentIntoStringConstructorArgument() {
        assertThat(beanWithTextResource.getTextAutowiredInConstructor())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
