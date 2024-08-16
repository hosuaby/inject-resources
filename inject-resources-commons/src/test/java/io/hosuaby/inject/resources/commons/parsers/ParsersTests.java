package io.hosuaby.inject.resources.commons.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.hosuaby.inject.resources.parsers.ParseWithGson;
import io.hosuaby.inject.resources.parsers.ParseWithJackson;
import io.hosuaby.inject.resources.parsers.Parsers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParsersTests {

    @Test
    @DisplayName("Test parse function from Jackson ObjectMapper")
    public void testParseFunction_fromJackson() {

        /* Given */
        var objectMapper = new ObjectMapper();

        /* When */
        var parseFunction = Parsers.parseFunction(objectMapper, Object.class);

        /* Then */
        assertThat(parseFunction)
                .isNotNull()
                .isInstanceOf(ParseWithJackson.class);
    }

    @Test
    @DisplayName("Test parse function from Gson")
    public void testParseFunction_fromGson() {

        /* Given */
        var gson = new Gson();

        /* When */
        var parseFunction = Parsers.parseFunction(gson, Object.class);

        /* Then */
        assertThat(parseFunction)
                .isNotNull()
                .isInstanceOf(ParseWithGson.class);
    }

    @Test
    @DisplayName("Test parse function from object of wrong type")
    public void testParseFunction_fromInvalidObject() {

        /* Given */
        var invalid = new Object();

        /* When */
        ThrowableAssert.ThrowingCallable createParseFunction = () -> Parsers.parseFunction(invalid, Object.class);

        /* Then */
        assertThatCode(createParseFunction)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong parser type: java.lang.Object.\nAccepted parser types:"
                        + "\n\t- com.fasterxml.jackson.databind.ObjectMapper"
                        + "\n\t- com.google.gson.Gson");
    }
}
