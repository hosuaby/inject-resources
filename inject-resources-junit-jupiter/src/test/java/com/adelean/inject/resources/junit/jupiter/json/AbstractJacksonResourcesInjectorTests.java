package com.adelean.inject.resources.junit.jupiter.json;

import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import com.adelean.inject.resources.junit.jupiter.WithGson;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
public class AbstractJacksonResourcesInjectorTests {

    @Nested
    @DisplayName("When there are two named Jackson & Gson mappers")
    class TestsWithNamedParsers {

        @WithJacksonMapper("jackson-mapper")
        ObjectMapper objectMapper = new ObjectMapper();

        @WithGson("gson-mapper")
        Gson gson = new Gson();

        @GivenJsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", jacksonMapper = "jackson-mapper")
        Map<String, Object> jsonAsMapByJackson;

        @GivenJsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", gson = "gson-mapper")
        Map<String, Object> jsonAsMapByGson;

        @Test
        @DisplayName("they both will be used")
        public void testRightParsersWasUsed() {
            assertThat(jsonAsMapByJackson)
                    .containsEntry("age", 22);  // Jackson recognize 'age' as integer
            assertThat(jsonAsMapByGson)
                    .containsEntry("age", 22.0d);   // Gson recognize 'age' as double
        }
    }

    @Nested
    @DisplayName("When there are two anonymous Jackson & Gson mappers")
    class TestsWithAnonymousParsers {

        @WithJacksonMapper
        ObjectMapper objectMapper = new ObjectMapper();

        @WithGson
        Gson gson = new Gson();

        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        @DisplayName("it's Jackson mapper that will be used")
        public void testJacksonMapperWasUsed() {
            assertThat(jsonAsMap)
                    .containsEntry("age", 22);  // Jackson recognize 'age' as integer
        }
    }

    @Nested
    @DisplayName("When there is a single anonymous Jackson mapper")
    class TestsWithSingleAnonymousJacksonMapper {

        @WithJacksonMapper
        ObjectMapper objectMapper = new ObjectMapper();

        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        @DisplayName("it will be used")
        public void testJacksonMapperWasUsed() {
            assertThat(jsonAsMap)
                    .containsEntry("age", 22);  // Jackson recognize 'age' as integer
        }
    }

    @Nested
    @DisplayName("When there is a single anonymous Gson mapper")
    class TestsWithSingleAnonymousGson {

        @WithGson
        Gson gson = new Gson();

        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        @DisplayName("it will be used")
        public void testGsonWasUsed() {
            assertThat(jsonAsMap)
                    .containsEntry("age", 22.0d);   // Gson recognize 'age' as double
        }
    }

    @Nested
    @DisplayName("When there is no declared mappers")
    class TestsWithoutDeclaredMappers {

        @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
        Map<String, Object> jsonAsMap;

        @Test
        @DisplayName("default Jackson mapper will be created and used if Jackson is on classpath")
        public void testJacksonMapperWasUsed() {
            assertThat(jsonAsMap)
                    .containsEntry("age", 22);  // Jackson recognize 'age' as integer
        }
    }
}
