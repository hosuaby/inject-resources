package io.hosuaby.junit.jupiter.resources;

import io.hosuaby.inject.resources.junit.jupiter.GivenPropertiesResource;
import io.hosuaby.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenPropertiesResource")
public class InjectPropertiesResourcesTests {

    @GivenPropertiesResource("/io/hosuaby/db.properties")
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
