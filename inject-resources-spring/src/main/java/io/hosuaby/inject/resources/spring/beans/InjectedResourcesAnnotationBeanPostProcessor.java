package io.hosuaby.inject.resources.spring.beans;

import io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement;
import io.hosuaby.inject.resources.spring.core.Annotations;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static io.hosuaby.inject.resources.spring.core.AbstractResourceInjectedElement.injectorForResource;

public class InjectedResourcesAnnotationBeanPostProcessor
        implements SmartInstantiationAwareBeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public PropertyValues postProcessProperties(
            PropertyValues pvs,
            Object bean,
            String beanName) throws BeansException {
        InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());

        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException beanCreationException) {
            throw beanCreationException;
        } catch (Throwable throwable) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", throwable);
        }

        return pvs;
    }

    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        if (!AnnotationUtils.isCandidateClass(clazz, Annotations.RESOURCE_ANNOTATIONS)) {
            return InjectionMetadata.EMPTY;
        }

        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        Class<?> targetClass = clazz;

        do {
            List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();

            ReflectionUtils.doWithLocalFields(targetClass, field -> {
                Annotation resourceAnnotation = Annotations.findSingleResourceAnnotation(field);
                if (resourceAnnotation != null) {
                      AbstractResourceInjectedElement<?> resourceFieldElement =
                            injectorForResource(resourceAnnotation, field, applicationContext);
                    currElements.add(resourceFieldElement);
                }
            });

            ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    return;
                }

                Annotation resourceAnnotation = Annotations.findSingleResourceAnnotation(bridgedMethod);
                if (resourceAnnotation != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                    AbstractResourceInjectedElement<?> resourceMethodElement =
                            injectorForResource(resourceAnnotation, method, applicationContext);
                    currElements.add(resourceMethodElement);
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return InjectionMetadata.forElements(elements, clazz);
    }
}
