package assertj.json.gson;

import org.assertj.core.api.SoftAssertions;
import com.google.gson.JsonElement;

public class JsonSoftAssertions extends SoftAssertions {
    public JsonElementAssert assertThat(JsonElement actual) {
        return proxy(JsonElementAssert.class, JsonElement.class, actual);
    }
}
