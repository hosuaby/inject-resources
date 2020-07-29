package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.JsonLinesResource;
import com.adelean.junit.jupiter.resources.data.gson.Log;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BeanWithJsonLinesResources {

    @JsonLinesResource(from = "/com/adelean/junit/jupiter/logs.jsonl", parserBean = "logsGson")
    private Log[] logsAsArray;

    @JsonLinesResource(from = "/com/adelean/junit/jupiter/logs.jsonl", parserBean = "logsGson")
    private Collection<Log> logsAsCollection;

    public Log[] getLogsAsArray() {
        return logsAsArray;
    }

    public Collection<Log> getLogsAsCollection() {
        return logsAsCollection;
    }
}
