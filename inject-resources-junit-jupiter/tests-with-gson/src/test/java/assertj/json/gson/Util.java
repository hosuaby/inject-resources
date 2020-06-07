package assertj.json.gson;

import com.google.gson.JsonElement;

class Util {
    static JsonElement findByPath(JsonElement root, String... path) {
        JsonElement current = root;
        for (String seg : path) {
            if (current == null) {
                return null;
            }
            if (current.isJsonObject()) {
                current = current.getAsJsonObject().get(seg);
            } else if (current.isJsonArray()) {
                int i = Integer.parseInt(seg);
                current = current.getAsJsonArray().get(i);
            } else {
                return null;
            }
        }
        return current;
    }
}
