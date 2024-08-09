package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.YamlResource;
import com.adelean.resources.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanWithYamlResources {

    @YamlResource("/com/adelean/junit/jupiter/receipt.yml")
    Map<String, Object> receipt;

    @YamlResource("/com/adelean/junit/jupiter/sponge-bob.yaml")
    Person spongeBob;

    final Person yamlAutowiredInConstructor;

    @Autowired
    public BeanWithYamlResources(
            @YamlResource("/com/adelean/junit/jupiter/sponge-bob.yaml")
            Person yamlAutowiredArgument) {
        this.yamlAutowiredInConstructor = yamlAutowiredArgument;
    }

    public Map<String, Object> getReceipt() {
        return receipt;
    }

    public Person getSpongeBob() {
        return spongeBob;
    }

    public Person getYamlAutowiredInConstructor() {
        return yamlAutowiredInConstructor;
    }
}
