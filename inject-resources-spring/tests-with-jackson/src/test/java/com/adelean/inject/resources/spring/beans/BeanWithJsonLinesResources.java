package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.JsonLinesResource;
import com.adelean.resources.data.jackson.Log;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BeanWithJsonLinesResources {

    @JsonLinesResource(from = "/com/adelean/junit/jupiter/logs.jsonl", parserBean = "logsObjectMapper")
    private Log[] logsAsArray;

    @JsonLinesResource(from = "/com/adelean/junit/jupiter/logs.jsonl", parserBean = "logsObjectMapper")
    private Collection<Log> logsAsCollection;

    public Log[] getLogsAsArray() {
        return logsAsArray;
    }

    public Collection<Log> getLogsAsCollection() {
        return logsAsCollection;
    }
}
