package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.TextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanWithTextResource {

    @TextResource("/io/hosuaby/resource.txt")
    private String text;

    private String textAutowiredInConstructor;

    @Autowired
    public BeanWithTextResource(
            @TextResource("/io/hosuaby/resource.txt")
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
