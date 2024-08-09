package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.TextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanWithTextResource {

    @TextResource("/com/adelean/junit/jupiter/resource.txt")
    private String text;

    private String textAutowiredInConstructor;

    @Autowired
    public BeanWithTextResource(
            @TextResource("/com/adelean/junit/jupiter/resource.txt")
            String textAutowiredArgument) {
        this.textAutowiredInConstructor = textAutowiredArgument;
    }

    public String getText() {
        return text;
    }

    public String getTextAutowiredInConstructor() {
        return textAutowiredInConstructor;
    }
}
