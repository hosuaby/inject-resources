package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.PropertiesResource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class BeanWithPropertiesResource {

    @PropertiesResource("/com/adelean/junit/jupiter/db.properties")
    private Properties dbProperties;

    public Properties getDbProperties() {
        return dbProperties;
    }
}
