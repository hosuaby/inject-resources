package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.JsonResource;
import io.hosuaby.resources.data.Person;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BeanWithJsonResources {

    @JsonResource("/io/hosuaby/sponge-bob.json")
    static Map<String, Object> jsonAsMap;

    @JsonResource(from = "/io/hosuaby/sponge-bob.json", parserBean = "defaultObjectMapper")
    JsonNode jsonNode;

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

    public JsonNode getJsonNode() {
        return jsonNode;
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
