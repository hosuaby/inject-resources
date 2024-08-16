package io.hosuaby.inject.resources.junit.vintage.helpers;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getConstructors;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withParametersCount;
import static org.reflections.ReflectionUtils.withTypeAssignableTo;

import io.hosuaby.inject.resources.junit.vintage.core.AbstractResourceRule;
import io.hosuaby.inject.resources.junit.vintage.core.ResourceRule;
import io.hosuaby.inject.resources.junit.vintage.GivenResource;
import org.junit.ClassRule;
import org.junit.Rule;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Java doesn't have reified generics. But this library offers to user concise syntax like:
 *
 * <pre>
 * &#64;Rule
 * public JsonResource&#60;Map&#60;String, Object&#62;&#62; jsonAsMap = givenResource()
 *         .json("/io/hosuaby/sponge-bob.json")
 *         .parseWith(objectMapper);
 * </pre>
 *
 * <p>Resource rule {@code JsonResource} is able to capture its generic argument {@code Map<String, Object>} and convert
 * content of JSON resource to {@code Map<String, Object>} without any type token being explicitly passed to the rule
 * builder.
 *
 * <p>So how it works?
 *
 * <p>JUnit 4 supplies test rules with {@link org.junit.runner.Description} object that gives us access to test class
 * with {@link org.junit.runner.Description#getTestClass()}. So we can get information about actual type parameters of
 * resource rules using reflexion.
 *
 * <p>But, we also need to be able associate resource rule objects with fields of test class containing them. This is
 * a little tricky. We know that JUnit 4 constrains test classes to have {@code public} zero-argument constructor.
 * So we are able to create an instance of a test class.
 *
 * <p>Each resource rule has attribute of type {@link CodeAnchor}, which contains a test class and the line number of
 * call to the factory method {@link GivenResource#givenResource()} that
 * created that rule.
 *
 * <p>So, to get generic parameter of the rule, we need to instantiate a new copy of test class, through reflexion find
 * the field of that instance that contains the rule with same {@code codeAnchor}, and then get generic information from
 * found field.
 *
 * @author Alexei KLENIN
 */
public final class ReifiedGenerics {
    private static final String ERR_MISSING_DEFAULT_CONSTRUCTOR =
            "Test class %s should have exactly one public zero-argument constructor.";
    private static final String ERR_CANNOT_FIND_MATCHING_RESOURCE_RULE =
            "Cannot find matching resource rule.";

    static final Map<Class<?>, Object> testInstances = new HashMap<>();

    private ReifiedGenerics() {
    }

    public static Type targetType(Class<?> testClass, ResourceRule<?> resourceRule) {
        @SuppressWarnings("unchecked")
        Set<Field> resourceClassRules = getAllFields(
                testClass,
                withModifier(Modifier.STATIC),
                withModifier(Modifier.PUBLIC),
                withAnnotation(ClassRule.class),
                withTypeAssignableTo(ResourceRule.class));

        Object testInstance = testInstance(testClass);

        @SuppressWarnings("unchecked")
        Set<Field> resourceInstanceRules = getAllFields(
                testClass,
                withModifier(Modifier.PUBLIC),
                withAnnotation(Rule.class),
                withTypeAssignableTo(ResourceRule.class));

        AbstractResourceRule<?> ruleObject = (AbstractResourceRule<?>) resourceRule;

        Optional<Field> targetField = resourceClassRules
                .stream()
                .filter(field -> ruleObject.isSame((AbstractResourceRule<?>) fieldValue(field, testClass)))
                .findAny();

        if (!targetField.isPresent()) {
            targetField = resourceInstanceRules
                    .stream()
                    .filter(field -> ruleObject.isSame((AbstractResourceRule<?>) fieldValue(field, testInstance)))
                    .findAny();
        }

        Field field = targetField.orElseThrow(() -> new RuntimeException(ERR_CANNOT_FIND_MATCHING_RESOURCE_RULE));

        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    static Object testInstance(Class<?> testClass) {
        return testInstances.computeIfAbsent(testClass, ReifiedGenerics::newInstance);
    }

    static Object newInstance(Class<?> testClass) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Set<Constructor> defaultConstructor = getConstructors(
                testClass, withModifier(Modifier.PUBLIC), withParametersCount(0));

        if (defaultConstructor.isEmpty()) {
            throw new RuntimeException(String.format(
                    ERR_MISSING_DEFAULT_CONSTRUCTOR, testClass.getName()));
        }

        try {
            return defaultConstructor.iterator().next().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    static Object fieldValue(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException unexpectedException) {
            throw new RuntimeException(unexpectedException);
        }
    }
}
