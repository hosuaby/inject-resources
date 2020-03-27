package assertj.json.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.AbstractBigIntegerAssert;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractByteAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractFloatAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractShortAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;

public class JsonElementAssert extends AbstractAssert<JsonElementAssert, JsonElement> {
    public JsonElementAssert(JsonElement actual) {
        super(actual, JsonElementAssert.class);
    }

    public static JsonElementAssert assertThat(JsonElement actual) {
        return new JsonElementAssert(actual);
    }

    public JsonElementAssert isObject() {
        isInstanceOf(JsonObject.class);
        return myself;
    }

    public JsonElementAssert isArray() {
        return isInstanceOf(JsonArray.class);
    }

    public JsonElementAssert isPrimitive() {
        return isInstanceOf(JsonPrimitive.class);
    }

    public JsonElementAssert isGsonNull() {
        return isInstanceOf(JsonNull.class);
    }

    public JsonElementAssert isString() {
        isPrimitive();
        if (!actual.getAsJsonPrimitive().isBoolean()) {
            failWithMessage(shouldBeBooleanButIs(actual.getAsString()));
        }
        return myself;
    }

    public JsonElementAssert isBoolean() {
        isPrimitive();
        if (!actual.getAsJsonPrimitive().isBoolean()) {
            failWithMessage(shouldBeBooleanButIs(actual.getAsString()));
        }
        return myself;
    }

    public JsonElementAssert isNumber() {
        isPrimitive();
        if (!actual.getAsJsonPrimitive().isNumber()) {
            failWithMessage(shouldBeNumberButIs(actual.getAsString()));
        }
        return myself;
    }

    public JsonElementAssert hasItem(int i) {
        isArray();
        if (i >= actual.getAsJsonArray().size()) {
            failWithMessage(shouldHaveItemWithIndex(i));
        }
        return JsonElementAssert.assertThat(actual.getAsJsonArray().get(i));
    }

    public JsonElementAssert doesNotHaveItem(int i) {
        isArray();
        if (i < actual.getAsJsonArray().size()) {
            failWithMessage(shouldNotHaveItemWithIndex(i));
        }
        return myself;
    }


    public JsonElementAssert hasField(String name) {
        isObject();
        if (!actual.getAsJsonObject().has(name)) {
            failWithMessage(shouldHaveFieldButDoesNot(name));
        }
        return JsonElementAssert.assertThat(actual.getAsJsonObject().get(name));
    }

    public JsonElementAssert doesNotHaveField(String name) {
        isObject();
        if (actual.getAsJsonObject().has(name)) {
            failWithMessage(shouldNotHaveFieldButHas(name));
        }
        return myself;
    }

    public JsonElementAssert hasPath(String... path) {
        JsonElement result = Util.findByPath(actual, path);
        if (result == null) {
            failWithMessage(pathNotFound(path));
        }
        return JsonElementAssert.assertThat(result);
    }

    public AbstractStringAssert<?> asString() {
        return Assertions.assertThat(actual.getAsString());
    }

    public AbstractIntegerAssert<?> asInt() {
        isNumber();
        return Assertions.assertThat(actual.getAsInt());
    }

    public AbstractLongAssert<?> asLong() {
        isNumber();
        return Assertions.assertThat(actual.getAsLong());
    }

    public AbstractFloatAssert<?> asFloat() {
        isNumber();
        return Assertions.assertThat(actual.getAsFloat());
    }

    public AbstractDoubleAssert<?> asDouble() {
        isNumber();
        return Assertions.assertThat(actual.getAsDouble());
    }

    public AbstractShortAssert<?> asShort() {
        isNumber();
        return Assertions.assertThat(actual.getAsShort());
    }

    public AbstractByteAssert<?> asByte() {
        isNumber();
        return Assertions.assertThat(actual.getAsByte());
    }

    public AbstractBooleanAssert<?> asBoolean() {
        isBoolean();
        return Assertions.assertThat(actual.getAsBoolean());
    }

    public AbstractBigIntegerAssert<?> asBigInteger() {
        isNumber();
        return Assertions.assertThat(actual.getAsBigInteger());
    }

    public AbstractBigDecimalAssert<?> asBigDecimal() {
        isNumber();
        return Assertions.assertThat(actual.getAsBigDecimal());
    }

    private String shouldHaveItemWithIndex(int i) {
        return String.format("Expected to have item at %d, but does not have it", i);
    }

    private String shouldNotHaveItemWithIndex(int i) {
        return String.format("Expected to do not have item at %d, but has it", i);
    }

    private String shouldHaveFieldButDoesNot(String name) {
        return String.format("Expected to have field %s, but does not have it", name);
    }

    private String shouldNotHaveFieldButHas(String name) {
        return String.format("Expected to do not have field %s, but has it", name);
    }

    private String pathNotFound(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String seg : path) {
            if (sb.length() > 0) {
                sb.append('/');
            }
            sb.append(seg);
        }
        return String.format("Element '%s' not found in json", sb);
    }

    private String shouldBeNumberButIs(String value) {
        return String.format("Expected to be a number, but is: %s", value);
    }

    private String shouldBeBooleanButIs(String value) {
        return String.format("Expected to be a boolean, but is: %s", value);
    }
}
