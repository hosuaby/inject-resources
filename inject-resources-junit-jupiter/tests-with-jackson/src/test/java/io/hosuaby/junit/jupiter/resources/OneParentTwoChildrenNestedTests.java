package io.hosuaby.junit.jupiter.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonResource;
import io.hosuaby.inject.resources.junit.jupiter.TestWithResources;
import io.hosuaby.inject.resources.junit.jupiter.WithJacksonMapper;
import io.hosuaby.resources.data.Person;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OneParentTwoChildrenNestedTests {

    class ParentContext {
        @WithJacksonMapper
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Nested
    @TestWithResources
    class FirstChildContext extends OneParentTwoChildrenStaticTests.ParentContext {
        @GivenJsonResource("/io/hosuaby/sponge-bob.json")
        Person spongeBob;

        @Test
        public void test() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @Nested
    @TestWithResources
    class SecondChildContext extends OneParentTwoChildrenStaticTests.ParentContext {
        @GivenJsonResource("/io/hosuaby/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        public void test() {
            assertThat(jsonAsMap)
                    .isNotNull();
        }
    }
}
