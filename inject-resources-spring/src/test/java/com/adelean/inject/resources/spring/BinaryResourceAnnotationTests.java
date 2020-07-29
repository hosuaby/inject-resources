package com.adelean.inject.resources.spring;

import static org.assertj.core.api.Assertions.assertThat;

import com.adelean.inject.resources.spring.beans.BeanWithBinaryResource;
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
@DisplayName("@BinaryResource")
public class BinaryResourceAnnotationTests {

    @Autowired
    private BeanWithBinaryResource beanWithBinaryResource;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithBinaryResource)
                .isNotNull();
    }

    @Test
    @DisplayName("injects binary content into byte[] class field")
    public void testInjectBinaryContentIntoByteArrayClassField() {
        assertThat(BeanWithBinaryResource.getFibonacciClassField())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("injects binary content into byte[] instance field")
    public void testInjectBinaryContentIntoByteArrayInstanceField() {
        assertThat(beanWithBinaryResource.getFibonacciInstanceField())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("injects binary content into byte[] setter method")
    public void testInjectBinaryContentIntoByteArraySetterMethod() {
        assertThat(beanWithBinaryResource.getFibonacciAutowiredArgument())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("injects binary content into byte[] constructor argument")
    public void testInjectBinaryContentIntoByteArrayConstructorArgument() {
        assertThat(beanWithBinaryResource.getFibonacciAutowiredInConstructor())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
}
