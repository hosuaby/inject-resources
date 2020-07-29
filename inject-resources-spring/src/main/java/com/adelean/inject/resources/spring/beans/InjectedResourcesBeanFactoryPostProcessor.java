package com.adelean.inject.resources.spring.beans;

import com.adelean.inject.resources.spring.core.AbstractResourceInjectedElement;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import static com.adelean.inject.resources.commons.FieldAsserts.assertSupportedType;
import static com.adelean.inject.resources.spring.core.AbstractResourceInjectedElement.injectorForResource;
import static com.adelean.inject.resources.spring.core.Annotations.annotationsToString;
import static com.adelean.inject.resources.spring.core.Annotations.findSingleResourceAnnotation;
import static com.adelean.inject.resources.spring.core.Annotations.invalidAnnotations;
import static com.adelean.inject.resources.spring.core.Asserts.assertNoOtherAnnotations;

public class InjectedResourcesBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Stream
                .of(beanFactory.getBeanDefinitionNames())
                .map(beanFactory::getBeanDefinition)
                .forEach(this::processBeanDefinition);
    }

    void processBeanDefinition(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getResolvableType().getRawClass();

        if (beanClass == null) {
            return;
        }

        Constructor<?>[] constructors = beanClass.getConstructors();

        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Annotation resourceAnnotation = findSingleResourceAnnotation(parameter);

                if (resourceAnnotation == null) {
                    continue;
                }

                assertNoOtherAnnotations(parameter, resourceAnnotation);

                Object value = resolveResourceArgument(parameter, resourceAnnotation);

                beanDefinition
                        .getConstructorArgumentValues()
                        .addIndexedArgumentValue(i, value);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Object resolveResourceArgument(Parameter parameter, Annotation resourceAnnotation) {
        Type valueType = parameter.getParameterizedType();
        AbstractResourceInjectedElement injectedElement = injectorForResource(
                resourceAnnotation, null, applicationContext);
        return injectedElement.valueToInject(valueType, resourceAnnotation);
    }
}
