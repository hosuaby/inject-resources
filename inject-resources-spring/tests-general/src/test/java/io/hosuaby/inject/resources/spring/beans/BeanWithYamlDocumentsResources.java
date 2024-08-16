package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.YamlDocumentsResource;
import io.hosuaby.resources.data.snakeyaml.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class BeanWithYamlDocumentsResources {

    @YamlDocumentsResource(from = "/io/hosuaby/stacktrace.yaml", yamlBean = "defaultYaml")
    List<Map<String, Object>> stacktraceAsList;

    @YamlDocumentsResource(from = "/io/hosuaby/stacktrace.yaml", yamlBean = "defaultYaml")
    Map<String, Object>[] stacktraceAsArray;

    @YamlDocumentsResource(from = "/io/hosuaby/logs.yml", yamlBean = "log-parser")
    Log[] logsAsArray;

    @YamlDocumentsResource(from = "/io/hosuaby/logs.yml", yamlBean = "log-parser")
    Collection<Log> logsAsCollection;

    final List<Map<String, Object>> yamlDocumentsAutowiredInConstructor;

    @Autowired
    public BeanWithYamlDocumentsResources(
            @YamlDocumentsResource(from = "/io/hosuaby/stacktrace.yaml", yamlBean = "defaultYaml")
            List<Map<String, Object>> yamlDocumentsAutowiredArgument) {
        this.yamlDocumentsAutowiredInConstructor = yamlDocumentsAutowiredArgument;
    }

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

    public List<Map<String, Object>> getYamlDocumentsAutowiredInConstructor() {
        return yamlDocumentsAutowiredInConstructor;
    }
}
