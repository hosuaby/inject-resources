package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.JsonResource;
import io.hosuaby.resources.data.Person;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BeanWithJsonResources {

    @JsonResource("/io/hosuaby/sponge-bob.json")
    private static Map<String, Object> jsonAsMap;

    @JsonResource("/io/hosuaby/sponge-bob.json")
    protected JsonElement jsonElement;

    Person spongeBob;
    List<Map<String, ?>> velibAsList;
    Map<String, ?>[] velibAsArray;

    @Autowired
    public BeanWithJsonResources(
            @JsonResource("/io/hosuaby/sponge-bob.json")
            Person spongeBob,
            @JsonResource("/io/hosuaby/velib.json")
            List<Map<String, ?>> velibAsList) {
        this.spongeBob = spongeBob;
        this.velibAsList = velibAsList;
    }

    @JsonResource("/io/hosuaby/velib.json")
    public void setVelibAsArray(Map<String, ?>[] velibAsArray) {
        this.velibAsArray = velibAsArray;
    }

    public static Map<String, Object> getJsonAsMap() {
        return jsonAsMap;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public Person getSpongeBob() {
        return spongeBob;
    }

    public List<Map<String, ?>> getVelibAsList() {
        return velibAsList;
    }

    public Map<String, ?>[] getVelibAsArray() {
        return velibAsArray;
    }
}
