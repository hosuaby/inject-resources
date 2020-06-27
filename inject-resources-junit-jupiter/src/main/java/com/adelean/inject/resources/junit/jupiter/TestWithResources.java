package com.adelean.inject.resources.junit.jupiter;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import org.apiguardian.api.API;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code TestWithResources} is a JUnit Jupiter extension that supplies your tests with loaded and parsed content of
 * resource files.
 *
 * <p>{@code TestWithResources} extension finds all fields and test method parameters annotated with
 * {@code Given*Resource} (a.k.a resource annotations) and sets their values to loaded and parsed content of
 * requested resource files.</p>
 *
 * <p>In some cases, resource injection requires an object (called parser), that will perform parsing of resource.
 * Those objects, provided by fields or methods annotated with {@code With*} (a.k.a parser annotations) are
 * discovered by extension prior to resource parsing.</p>
 *
 * <p>The annotation {@code @TestWithResources} can be used on a superclass in the test hierarchy as well. All
 * subclasses will automatically inherit support for the extension.</p>
 *
 * <p>Example:</p>
 *
 * <pre>
 * &#64;TestWithResources
 * class InjectTextResourcesTests {
 *
 *     &#64;GivenTextResource("/com/adelean/junit/jupiter/resource.txt")
 *     String instanceField;
 *
 *     &#64;Test
 *     public void testInjectTextIntoStringInstanceField() {
 *         assertThat(instanceField)
 *                 .isEqualTo("The quick brown fox jumps over the lazy dog.");
 *     }
 * }
 * </pre>
 *
 * @author Alexei KLENIN
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TestWithResourcesExtension.class)
@Inherited
@Documented
@API(status = EXPERIMENTAL, since = "0.1")
public @interface TestWithResources {
}
