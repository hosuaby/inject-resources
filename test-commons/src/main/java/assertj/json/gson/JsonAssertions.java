package assertj.json.gson;

import com.google.gson.JsonElement;
import org.assertj.core.api.Assertions;

public class JsonAssertions extends Assertions {
    public static JsonElementAssert assertThat(JsonElement actual) {
        return JsonElementAssert.assertThat(actual);
    }
}
