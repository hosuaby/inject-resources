package io.hosuaby.inject.resources.spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceInjectionConfiguration {

    @Bean
    public InjectedResourcesAnnotationBeanPostProcessor injectedResourcesAnnotationBeanPostProcessor() {
        return new InjectedResourcesAnnotationBeanPostProcessor();
    }

    @Bean
    public InjectedResourcesBeanFactoryPostProcessor injectedResourcesBeanFactoryPostProcessor() {
        return new InjectedResourcesBeanFactoryPostProcessor();
    }
}
