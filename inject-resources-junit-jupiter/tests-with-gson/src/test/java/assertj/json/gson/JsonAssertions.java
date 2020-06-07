package assertj.json.gson;

import org.assertj.core.api.Assertions;
import com.google.gson.JsonElement;

public class JsonAssertions extends Assertions {
    public static JsonElementAssert assertThat(JsonElement actual) {
        return JsonElementAssert.assertThat(actual);
    }
}
