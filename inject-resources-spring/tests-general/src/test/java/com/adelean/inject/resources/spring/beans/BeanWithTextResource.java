package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.TextResource;
import org.springframework.stereotype.Component;

@Component
public class BeanWithTextResource {

    @TextResource("/com/adelean/junit/jupiter/resource.txt")
    private String text;

    public String getText() {
        return text;
    }
}
