package assertj.json.gson;

import com.google.gson.JsonElement;
import org.assertj.core.api.SoftAssertions;

public class JsonSoftAssertions extends SoftAssertions {
    public JsonElementAssert assertThat(JsonElement actual) {
        return proxy(JsonElementAssert.class, JsonElement.class, actual);
    }
}
