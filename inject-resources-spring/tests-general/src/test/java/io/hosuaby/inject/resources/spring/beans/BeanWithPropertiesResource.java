package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.PropertiesResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class BeanWithPropertiesResource {

    @PropertiesResource("/io/hosuaby/db.properties")
    private Properties dbProperties;

    private Properties dbPropertiesAutowiredInConstructor;

    @Autowired
    public BeanWithPropertiesResource(
            @PropertiesResource("/io/hosuaby/db.properties")
            Properties dbPropertiesAutowiredArgument) {
        this.dbPropertiesAutowiredInConstructor = dbPropertiesAutowiredArgument;
    }

    public Properties getDbProperties() {
        return dbProperties;
    }

    public Properties getDbPropertiesAutowiredInConstructor() {
        return dbPropertiesAutowiredInConstructor;
    }
}
