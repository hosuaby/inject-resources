package com.adelean.inject.resources.spring;

import com.adelean.inject.resources.spring.beans.BeanWithPropertiesResource;
import com.adelean.inject.resources.spring.beans.TestConfig;
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
@DisplayName("@PropertiesResource")
public class PropertiesResourceAnnotationTests {

    @Autowired
    private BeanWithPropertiesResource beanWithPropertiesResource;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithPropertiesResource)
                .isNotNull();
    }

    @Test
    @DisplayName("injects properties content into Properties instance field")
    public void testInjectPropertiesContentIntoPropertiesClassField() {
        assertThat(beanWithPropertiesResource.getDbProperties())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }

    @Test
    @DisplayName("injects properties content into Properties constructor argument")
    public void testInjectPropertiesContentIntoPropertiesConstructorArgument() {
        assertThat(beanWithPropertiesResource.getDbPropertiesAutowiredInConstructor())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }
}
