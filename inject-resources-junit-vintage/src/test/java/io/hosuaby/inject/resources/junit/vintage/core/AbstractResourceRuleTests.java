package io.hosuaby.inject.resources.junit.vintage.core;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class AbstractResourceRuleTests {

    @Rule
    public AbstractResourceRule<String> firstTextRule = givenResource()
            .text("/io/hosuaby/resource.txt")
            .withCharset(StandardCharsets.UTF_8);

    @Rule
    public AbstractResourceRule<String> secondTextRule = givenResource()
            .text("/io/hosuaby/resource.txt")
            .withCharset(StandardCharsets.UTF_8);

    @Test
    public void testIsSame() {
        assertThat(firstTextRule.isSame(firstTextRule))
                .isTrue();
        assertThat(firstTextRule.isSame(secondTextRule))
                .isFalse();
    }
}
