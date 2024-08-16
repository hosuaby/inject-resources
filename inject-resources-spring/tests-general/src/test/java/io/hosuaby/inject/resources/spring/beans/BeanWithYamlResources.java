package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.YamlResource;
import io.hosuaby.resources.data.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanWithYamlResources {

    @YamlResource("/io/hosuaby/receipt.yml")
    Map<String, Object> receipt;

    @YamlResource("/io/hosuaby/sponge-bob.yaml")
    Person spongeBob;

    final Person yamlAutowiredInConstructor;

    @Autowired
    public BeanWithYamlResources(
            @YamlResource("/io/hosuaby/sponge-bob.yaml")
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
