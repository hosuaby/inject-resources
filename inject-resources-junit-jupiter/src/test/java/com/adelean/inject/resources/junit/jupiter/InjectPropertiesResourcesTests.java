package com.adelean.inject.resources.junit.jupiter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@TestWithResources
@DisplayName("@GivenPropertiesResource")
public class InjectPropertiesResourcesTests {

    @GivenPropertiesResource("/com/adelean/junit/jupiter/db.properties")
    Properties dbProperties;

    @Test
    @DisplayName("loads properties file into Properties object")
    public void testInjectTextIntoStringInstanceField() {
        assertThat(dbProperties)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }
}
