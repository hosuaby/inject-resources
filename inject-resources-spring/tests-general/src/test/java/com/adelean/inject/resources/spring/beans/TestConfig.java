package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.EnableResourceInjection;
import com.adelean.resources.data.snakeyaml.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Configuration
@EnableResourceInjection
@ComponentScan(basePackageClasses = TestConfig.class)
public class TestConfig {

    @Bean
    @Primary
    public Yaml defaultYaml() {
        return new Yaml();
    }

    @Bean("log-parser")
    public Yaml logYaml() {
        return new Yaml(new Constructor(Log.class, new LoaderOptions()));
    }
}
