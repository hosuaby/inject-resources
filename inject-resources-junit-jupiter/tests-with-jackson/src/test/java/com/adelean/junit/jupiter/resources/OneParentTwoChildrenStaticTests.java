package com.adelean.junit.jupiter.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.adelean.resources.data.Person;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OneParentTwoChildrenStaticTests {

    static class ParentContext {
        @WithJacksonMapper
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @TestWithResources
    static class FirstChildContext extends ParentContext {
        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Person spongeBob;

        @Test
        public void test() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @TestWithResources
    static class SecondChildContext extends ParentContext {
        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        public void test() {
            assertThat(jsonAsMap)
                    .isNotNull();
        }
    }
}
