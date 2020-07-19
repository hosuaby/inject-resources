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
    @DisplayName("injects content of properties resource file into Properties instance field")
    public void testInjectPropertiesIntoPropertiesInstanceField() {
        assertThat(dbProperties)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }
}
