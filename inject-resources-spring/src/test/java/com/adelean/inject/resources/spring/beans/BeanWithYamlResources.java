package com.adelean.inject.resources.spring.beans;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.adelean.inject.resources.spring.YamlResource;
import com.adelean.junit.jupiter.resources.data.Person;

@Component
public class BeanWithYamlResources {

    @YamlResource("/com/adelean/junit/jupiter/receipt.yml")
    Map<String, Object> receipt;

    @YamlResource("/com/adelean/junit/jupiter/sponge-bob.yaml")
    Person spongeBob;

    public Map<String, Object> getReceipt() {
        return receipt;
    }

    public Person getSpongeBob() {
        return spongeBob;
    }
}
