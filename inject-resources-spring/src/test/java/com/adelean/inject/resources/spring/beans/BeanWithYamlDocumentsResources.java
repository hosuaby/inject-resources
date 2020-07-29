package com.adelean.inject.resources.spring.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.adelean.inject.resources.spring.YamlDocumentsResource;
import com.adelean.junit.jupiter.resources.data.snakeyaml.Log;

@Component
public class BeanWithYamlDocumentsResources {

    @YamlDocumentsResource(from = "/com/adelean/junit/jupiter/stacktrace.yaml", yamlBean = "defaultYaml")
    List<Map<String, Object>> stacktraceAsList;

    @YamlDocumentsResource(from = "/com/adelean/junit/jupiter/stacktrace.yaml", yamlBean = "defaultYaml")
    Map<String, Object>[] stacktraceAsArray;

    @YamlDocumentsResource(from = "/com/adelean/junit/jupiter/logs.yml", yamlBean = "log-parser")
    Log[] logsAsArray;

    @YamlDocumentsResource(from = "/com/adelean/junit/jupiter/logs.yml", yamlBean = "log-parser")
    Collection<Log> logsAsCollection;

    public List<Map<String, Object>> getStacktraceAsList() {
        return stacktraceAsList;
    }

    public Map<String, Object>[] getStacktraceAsArray() {
        return stacktraceAsArray;
    }

    public Log[] getLogsAsArray() {
        return logsAsArray;
    }

    public Collection<Log> getLogsAsCollection() {
        return logsAsCollection;
    }
}
