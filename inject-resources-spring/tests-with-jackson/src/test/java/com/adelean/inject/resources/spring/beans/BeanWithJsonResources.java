package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.JsonResource;
import com.adelean.resources.data.Person;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BeanWithJsonResources {

    @JsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    static Map<String, Object> jsonAsMap;

    @JsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", parserBean = "defaultObjectMapper")
    JsonNode jsonNode;

    Person spongeBob;
    List<Map<String, ?>> velibAsList;
    Map<String, ?>[] velibAsArray;

    @Autowired
    public BeanWithJsonResources(
            @JsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
            Person spongeBob,
            @JsonResource("/com/adelean/junit/jupiter/velib.json")
            List<Map<String, ?>> velibAsList) {
        this.spongeBob = spongeBob;
        this.velibAsList = velibAsList;
    }

    @JsonResource("/com/adelean/junit/jupiter/velib.json")
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
