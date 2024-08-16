package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.bytecode.ParameterAnnotationsAttribute;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement.injectorForResource;
import static io.hosuaby.inject.resources.spring.core.Annotations.assertNoOtherAnnotations;
import static io.hosuaby.inject.resources.spring.core.Annotations.findSingleResourceAnnotation;
import static io.hosuaby.inject.resources.spring.core.Annotations.isResourceAnnotation;

public class InjectedResourcesBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
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
        findBeanClass(beanDefinition)
                .map(this::getInjectedParameters)
                .orElseGet(Collections::emptyMap)
                .forEach(beanDefinition.getConstructorArgumentValues()::addIndexedArgumentValue);
    }

    Map<Integer, Object> getInjectedParameters(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getConstructors();

        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            Map<Integer, Object> parameterMap = new HashMap<>();

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Annotation resourceAnnotation = findSingleResourceAnnotation(parameter);

                if (resourceAnnotation == null) {
                    continue;
                }

                assertNoOtherAnnotations(parameter, resourceAnnotation);
                Object value = resolveResourceArgument(parameter.getParameterizedType(), resourceAnnotation);
                parameterMap.put(i, value);
            }

            if (!parameterMap.isEmpty()) {
                return parameterMap;
            }
        }

        return Collections.emptyMap();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Object resolveResourceArgument(Type valueType, Annotation resourceAnnotation) throws BeansException {
        AbstractResourceInjectedElement injectedElement = injectorForResource(
                resourceAnnotation, null, applicationContext);
        return injectedElement.valueToInject(valueType, resourceAnnotation);
    }

    static Optional<Class<?>> findBeanClass(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getResolvableType().getRawClass();
        String beanClassName = beanDefinition.getBeanClassName();

        if (beanClass != null) {
            return Optional.of(beanClass);
        } else if (beanClassName != null && hasResourcesInjectedInConstructor(beanClassName)) {
            try {
                beanClass = Class.forName(beanClassName);
                return Optional.of(beanClass);
            } catch (ClassNotFoundException classNotFoundException) {
                throw new FatalBeanException(classNotFoundException.getMessage(), classNotFoundException);
            }
        } else {
            return Optional.empty();
        }
    }

    static boolean hasResourcesInjectedInConstructor(String beanClassName) {
        try {
            CtClass ctClass = CLASS_POOL.get(beanClassName);
            CtConstructor[] constructors = ctClass.getDeclaredConstructors();

            for (CtConstructor constructor : constructors) {
                ParameterAnnotationsAttribute parameterAnnotationsAttribute =
                        (ParameterAnnotationsAttribute) constructor
                                .getMethodInfo()
                                .getAttribute(ParameterAnnotationsAttribute.visibleTag);

                if (parameterAnnotationsAttribute != null) {
                    javassist.bytecode.annotation.Annotation[][] annotations =
                            parameterAnnotationsAttribute.getAnnotations();

                    for (javassist.bytecode.annotation.Annotation[] parameterAnnotations : annotations) {
                        for (javassist.bytecode.annotation.Annotation annotation : parameterAnnotations) {
                            if (isResourceAnnotation(annotation.getTypeName())) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (NotFoundException classNotFoundException) {
            throw new FatalBeanException(classNotFoundException.getMessage(), classNotFoundException);
        }

        return false;
    }
}
