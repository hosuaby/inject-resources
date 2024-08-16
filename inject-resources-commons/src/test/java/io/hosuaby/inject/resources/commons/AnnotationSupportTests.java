package io.hosuaby.inject.resources.commons;

import static io.leangen.geantyref.TypeFactory.annotation;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import io.hosuaby.inject.resources.annotations.Extends;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import io.hosuaby.inject.resources.annotations.Named;
import io.hosuaby.inject.resources.annotations.Parser;
import io.hosuaby.inject.resources.annotations.Resource;
import io.hosuaby.inject.resources.annotations.WithPath;
import io.leangen.geantyref.AnnotationFormatException;

public class AnnotationSupportTests {

    @Test
    @DisplayName("Test get 'from' from resource annotation when only 'from' is defined")
    public void testGetFrom_fromDefined() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, singletonMap("from", "/path/to/resource"));

        /* When */
        String from = AnnotationSupport.getFrom(annotation);

        /* Then */
        assertThat(from)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("/path/to/resource");
    }

    @Test
    @DisplayName("Test get 'from' from resource annotation when only 'value' is defined")
    public void testGetFrom_valueDefined() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, singletonMap("value", "/path/to/resource"));

        /* When */
        String from = AnnotationSupport.getFrom(annotation);

        /* Then */
        assertThat(from)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("/path/to/resource");
    }

    @Test
    @DisplayName("Test get 'from' from resource annotation when both 'from' & 'value' are defined")
    public void testGetFrom_fromAndValueDefined() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, Map.of(
                "from", "/path/to/resource1",
                "value", "/path/to/resource2"));

        /* When */
        String from = AnnotationSupport.getFrom(annotation);

        /* Then */
        assertThat(from)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("/path/to/resource1");
    }

    @Test
    @DisplayName("Test get 'from' from resource annotation when nether 'from' or 'value' are defined")
    public void testGetFrom_fromAndValueUndefined() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, emptyMap());

        /* When */
        String from = AnnotationSupport.getFrom(annotation);

        /* Then */
        assertThat(from)
                .isNull();
    }

    @Test
    @DisplayName("Test get value by invoking method returning non blank value")
    public void testValueFromMethod_nonBlankValue() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, singletonMap("from", "/path/to/resource"));
        Method fromMethod = ReflectionSupport
                .findMethod(GivenBinaryResource.class, "from")
                .get();

        /* When */
        Optional<String> fromValue = AnnotationSupport.valueFromMethod(fromMethod, annotation);

        /* Then */
        assertThat(fromValue)
                .isNotNull()
                .isNotEmpty()
                .contains("/path/to/resource");
    }

    @Test
    @DisplayName("Test get value by invoking method returning blank value")
    public void testValueFromMethod_blankValue() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, singletonMap("from", "   "));
        Method fromMethod = ReflectionSupport
                .findMethod(GivenBinaryResource.class, "from")
                .get();

        /* When */
        Optional<String> fromValue = AnnotationSupport.valueFromMethod(fromMethod, annotation);

        /* Then */
        assertThat(fromValue)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Test get name from annotation with name")
    public void testNameFromAnnotation_withName() throws Exception {

        /* Given */
        var annotation = annotation(AnnotationWithName.class, singletonMap("name", "some-name"));

        /* When */
        String parserName = AnnotationSupport.getName(annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("some-name");
    }

    @Test
    @DisplayName("Test get name from annotation with value")
    public void testNameFromAnnotation_withValue() throws Exception {

        /* Given */
        var annotation = annotation(AnnotationWithName.class, singletonMap("value", "some-name"));

        /* When */
        String parserName = AnnotationSupport.getName(annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("some-name");
    }

    @Test
    @DisplayName("Test get name from annotation without name nor value")
    public void testNameFromAnnotation_invalidAnnotation() throws Exception {

        /* Given */
        var annotation = annotation(AnnotationWithoutName.class, emptyMap());

        /* When */
        ThrowableAssert.ThrowingCallable callable = () -> AnnotationSupport.getName(annotation);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("INTERNAL ERROR: @AnnotationWithoutName missing methods: [String name()], " +
                        "[String value()]. Please, open an issue on Github repository of project " +
                        "'junit-jupiter-resources'.");
    }

    @Test
    @DisplayName("Test assert annotation extends - success")
    public void testAssertAnnotationExtends_extends() {
        assertThatCode(() -> AnnotationSupport.assertAnnotationExtends(GivenBinaryResource.class, WithPath.class))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test assert annotation extends - failure")
    public void testAssertAnnotationExtends_notExtends() {
        assertThatCode(() -> AnnotationSupport.assertAnnotationExtends(GivenBinaryResource.class, Named.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("INTERNAL ERROR: @GivenBinaryResource missing @Extends(Named.class). Please, open an issue "
                        + "on Github repository of project 'junit-jupiter-resources'.");
    }

    @Test
    @DisplayName("Test is annotation extends")
    public void testIsAnnotationExtends() {
        assertThat(AnnotationSupport.isAnnotationExtends(GivenBinaryResource.class, WithPath.class))
                .isTrue();
        assertThat(AnnotationSupport.isAnnotationExtends(GivenBinaryResource.class, Named.class))
                .isFalse();
    }

    @Test
    @DisplayName("Test annotation inheritance - success")
    public void testAssertInheritProperly_success() {
        assertThatCode(() -> AnnotationSupport.assertInheritProperly(GivenBinaryResource.class))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test annotation inheritance - failure")
    public void testAssertInheritProperly_failure() {
        assertThatCode(() -> AnnotationSupport.assertInheritProperly(GivenWrongResource.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("INTERNAL ERROR: @GivenWrongResource missing methods: [String from()]. Please, open an "
                        +"issue on Github repository of project 'junit-jupiter-resources'.");
    }

    @Test
    @DisplayName("Test is annotation has method")
    public void testIsHasMethod_methodPresent() {

        /* Given */
        Method fromMethod = ReflectionSupport
                .findMethod(WithPath.class, "from")
                .get();

        /* When */
        boolean givenBinaryHasMethod = AnnotationSupport.isHasMethod(GivenBinaryResource.class, fromMethod);
        boolean givenWrongHasMethod = AnnotationSupport.isHasMethod(GivenWrongResource.class, fromMethod);

        /* Then */
        assertThat(givenBinaryHasMethod)
                .isTrue();
        assertThat(givenWrongHasMethod)
                .isFalse();
    }

    @Resource
    @interface GivenBinaryResource {
        String value() default "";
        String from() default "";
    }

    @Resource
    @interface GivenWrongResource {
        String value() default "";
    }

    @Parser
    @interface WithWrongParser {
    }

    @Extends(Named.class)
    @interface AnnotationWithName {
        String name() default "";
        String value() default "";
    }

    @Extends(Named.class)
    @interface AnnotationWithoutName {
    }
}
