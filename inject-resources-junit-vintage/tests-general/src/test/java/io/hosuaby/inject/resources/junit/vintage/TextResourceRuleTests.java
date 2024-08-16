package io.hosuaby.inject.resources.junit.vintage;

import io.hosuaby.inject.resources.junit.vintage.core.ResourceRule;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

public class TextResourceRuleTests {

    @Rule
    public ResourceRule<String> textResource = givenResource()
            .text("/io/hosuaby/resource.txt")
            .withCharset(StandardCharsets.UTF_8);

    @Test
    public void testLoadTextIntoString() {
        assertThat(textResource.get())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
