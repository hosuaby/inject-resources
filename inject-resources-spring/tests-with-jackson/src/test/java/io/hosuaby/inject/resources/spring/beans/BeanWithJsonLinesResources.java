package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.JsonLinesResource;
import io.hosuaby.resources.data.jackson.Log;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class BeanWithJsonLinesResources {

    @JsonLinesResource(from = "/io/hosuaby/logs.jsonl", parserBean = "logsObjectMapper")
    private Log[] logsAsArray;

    @JsonLinesResource(from = "/io/hosuaby/logs.jsonl", parserBean = "logsObjectMapper")
    private Collection<Log> logsAsCollection;

    public Log[] getLogsAsArray() {
        return logsAsArray;
    }

    public Collection<Log> getLogsAsCollection() {
        return logsAsCollection;
    }
}
