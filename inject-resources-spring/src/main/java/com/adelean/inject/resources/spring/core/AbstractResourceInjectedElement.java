package com.adelean.inject.resources.spring.core;

import com.adelean.inject.resources.spring.EnableResourceInjection;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static com.adelean.inject.resources.commons.FieldAsserts.assertSupportedType;
import static com.adelean.inject.resources.spring.core.Annotations.assertNoOtherAnnotations;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractResourceInjectedElement<A extends Annotation> extends InjectionMetadata.InjectedElement {
    public static final Reflections REFLECTIONS = new Reflections(new ConfigurationBuilder()
            .forPackage(EnableResourceInjection.class.getPackage().getName())
            .setClassLoaders(new ClassLoader[]{ EnableResourceInjection.class.getClassLoader() }));

    private static final Map<
            Class<? extends Annotation>,
            Class<? extends AbstractResourceInjectedElement<? extends Annotation>>> INJECTED_ELEMENT_CLASSES =
                    getAllFieldElements();

    protected final A resourceAnnotation;
    protected final ApplicationContext applicationContext;

    protected AbstractResourceInjectedElement(
            Member member,
            A resourceAnnotation,
            ApplicationContext applicationContext) {
        super(member, null);
        this.resourceAnnotation = resourceAnnotation;
        this.applicationContext = applicationContext;
    }

    @Override
    public final void inject(
            @NotNull Object bean,
            @Nullable String beanName,
            @Nullable PropertyValues pvs) throws Throwable {
        if (this.member instanceof Field) {
            injectField(bean);
        } else if (this.member instanceof Method) {
            injectMethod(bean);
        }
    }

    protected void injectField(Object bean) throws Throwable {
        Field field = (Field) this.member;
        assertValidField(field);
        Type valueType = field.getGenericType();

        Object value = valueToInject(valueType, resourceAnnotation);

        ReflectionUtils.makeAccessible(field);
        field.set(bean, value);
    }

    protected void injectMethod(Object bean) throws Throwable {
        Method method = (Method) this.member;
        assertValidMethod(method);

        Type valueType = method.getGenericParameterTypes()[0];
        Object value = valueToInject(valueType, resourceAnnotation);

        try {
            ReflectionUtils.makeAccessible(method);
            method.invoke(bean, value);
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        }
    }

    protected void assertValidField(Field field) {
        @SuppressWarnings("unchecked")
        Class<A> annotationType = (Class<A>) resourceAnnotation.annotationType();
        assertNoOtherAnnotations(field, resourceAnnotation);
        assertSupportedType(field, annotationType);
    }

    protected void assertValidMethod(Method method) {
        assertNoOtherAnnotations(method, resourceAnnotation);

        @SuppressWarnings("unchecked")
        Class<A> annotationType = (Class<A>) resourceAnnotation.annotationType();

        if (method.getParameterCount() == 1) {
            Parameter parameter = method.getParameters()[0];
            assertSupportedType(parameter, annotationType);
        } else {
            throw new RuntimeException(String.format(
                    "Method %s annotated with @%s must have exactly one parameter",
                    method.getName(),
                    annotationType.getSimpleName()));
        }
    }

    abstract public Object valueToInject(Type valueType, A resourceAnnotation);

    public static AbstractResourceInjectedElement<?> injectorForResource(
            Annotation resourceAnnotation,
            Member member,
            ApplicationContext applicationContext) {
        Class<? extends AbstractResourceInjectedElement<?>> resourceFieldElementClass = INJECTED_ELEMENT_CLASSES
                .get(resourceAnnotation.annotationType());

        try {
            @SuppressWarnings("unchecked")
            Constructor<AbstractResourceInjectedElement<?>> constructor =
                    (Constructor<AbstractResourceInjectedElement<?>>) resourceFieldElementClass.getConstructors()[0];
            constructor.setAccessible(true);

            return constructor.newInstance(member, resourceAnnotation, applicationContext);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException instantiationException) {
            throw new RuntimeException(instantiationException);
        }
    }

    @SuppressWarnings("unchecked")
    static Map<
            Class<? extends Annotation>,
            Class<? extends AbstractResourceInjectedElement<? extends Annotation>>> getAllFieldElements() {
        return REFLECTIONS
                .getSubTypesOf(AbstractResourceInjectedElement.class)
                .stream()
                .filter(clazz -> Modifier.isPublic(clazz.getModifiers()))
                .map(clazz -> (Class<? extends AbstractResourceInjectedElement<? extends Annotation>>) clazz)
                .collect(toMap(
                        clazz -> (Class<? extends Annotation>)
                                ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0],
                        clazz -> clazz));
    }
}
