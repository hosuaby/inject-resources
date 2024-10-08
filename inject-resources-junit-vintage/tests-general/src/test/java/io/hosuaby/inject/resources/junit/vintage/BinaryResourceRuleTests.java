package io.hosuaby.inject.resources.junit.vintage;

import io.hosuaby.inject.resources.junit.vintage.core.ResourceRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

public class BinaryResourceRuleTests {

    @ClassRule
    public static ResourceRule<byte[]> fibonacciStatic = givenResource()
            .binary("/io/hosuaby/fibonacci.bin");

    @Rule
    public ResourceRule<byte[]> fibonacci = givenResource()
            .binary("/io/hosuaby/fibonacci.bin");

    @Test
    public void testLoadBinaryContentIntoClassRule() {
        assertThat(fibonacciStatic.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    public void testLoadBinaryContentIntoInstanceRule() {
        assertThat(fibonacci.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
}
