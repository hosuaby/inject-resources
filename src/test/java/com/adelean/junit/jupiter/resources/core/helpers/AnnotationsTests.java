package com.adelean.junit.jupiter.resources.core.helpers;

import static io.leangen.geantyref.TypeFactory.annotation;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Method;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import com.adelean.junit.jupiter.resources.GivenBinaryResource;
import com.adelean.junit.jupiter.resources.WithJacksonMapper;
import com.adelean.junit.jupiter.resources.commons.AnnotationWithName;
import com.adelean.junit.jupiter.resources.commons.AnnotationWithoutName;
import com.adelean.junit.jupiter.resources.core.annotations.Named;
import com.adelean.junit.jupiter.resources.core.annotations.Parser;
import com.adelean.junit.jupiter.resources.core.annotations.Resource;
import com.adelean.junit.jupiter.resources.core.annotations.WithPath;
import com.google.common.collect.ImmutableMap;
import io.leangen.geantyref.AnnotationFormatException;
import io.leangen.geantyref.TypeFactory;

public class AnnotationsTests {

    @Test
    @DisplayName("Test get 'from' from resource annotation when only 'from' is defined")
    public void testGetFrom_fromDefined() throws AnnotationFormatException {

        /* Given */
        var annotation = annotation(GivenBinaryResource.class, singletonMap("from", "/path/to/resource"));

        /* When */
        String from = Annotations.getFrom(annotation);

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
        String from = Annotations.getFrom(annotation);

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
        var annotation = annotation(GivenBinaryResource.class, ImmutableMap.of(
                "from", "/path/to/resource1",
                "value", "/path/to/resource2"));

        /* When */
        String from = Annotations.getFrom(annotation);

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
        String from = Annotations.getFrom(annotation);

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
        Optional<String> fromValue = Annotations.valueFromMethod(fromMethod, annotation);

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
        Optional<String> fromValue = Annotations.valueFromMethod(fromMethod, annotation);

        /* Then */
        assertThat(fromValue)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Test get name from annotation with name")
    public void testNameFromAnnotation_withName() throws Exception {

        /* Given */
        var annotation = TypeFactory.annotation(AnnotationWithName.class, singletonMap("name", "some-name"));

        /* When */
        String parserName = Annotations.getName(annotation);

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
        var annotation = TypeFactory.annotation(AnnotationWithName.class, singletonMap("value", "some-name"));

        /* When */
        String parserName = Annotations.getName(annotation);

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
        var annotation = TypeFactory.annotation(AnnotationWithoutName.class, emptyMap());

        /* When */
        ThrowableAssert.ThrowingCallable callable = () -> Annotations.getName(annotation);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("INTERNAL ERROR: @AnnotationWithoutName missing methods: [String value()], " +
                        "[String name()]. Please, open an issue on Github repository of project " +
                        "'junit-jupiter-resources'.");
    }

    @Test
    @DisplayName("Test assert annotation extends - success")
    public void testAssertAnnotationExtends_extends() {
        assertThatCode(() -> Annotations.assertAnnotationExtends(GivenBinaryResource.class, WithPath.class))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test assert annotation extends - failure")
    public void testAssertAnnotationExtends_notExtends() {
        assertThatCode(() -> Annotations.assertAnnotationExtends(GivenBinaryResource.class, Named.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("INTERNAL ERROR: @GivenBinaryResource missing @Extends(Named.class). Please, open an issue "
                        + "on Github repository of project 'junit-jupiter-resources'.");
    }

    @Test
    @DisplayName("Test is annotation extends")
    public void testIsAnnotationExtends() {
        assertThat(Annotations.isAnnotationExtends(GivenBinaryResource.class, WithPath.class))
                .isTrue();
        assertThat(Annotations.isAnnotationExtends(GivenBinaryResource.class, Named.class))
                .isFalse();
    }

    @Test
    @DisplayName("Test annotation inheritance - success")
    public void testAssertInheritProperly_success() {
        assertThatCode(() -> Annotations.assertInheritProperly(GivenBinaryResource.class))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test annotation inheritance - failure")
    public void testAssertInheritProperly_failure() {
        assertThatCode(() -> Annotations.assertInheritProperly(GivenWrongResource.class))
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
        boolean givenBinaryHasMethod = Annotations.isHasMethod(GivenBinaryResource.class, fromMethod);
        boolean givenWrongHasMethod = Annotations.isHasMethod(GivenWrongResource.class, fromMethod);

        /* Then */
        assertThat(givenBinaryHasMethod)
                .isTrue();
        assertThat(givenWrongHasMethod)
                .isFalse();
    }

    @Test
    @DisplayName("Test is annotation is resource annotation")
    public void testIsResourceAnnotation_validResourceAnnotation() {
        assertThat(Annotations.isResourceAnnotation(GivenBinaryResource.class))
                .isTrue();
        assertThat(Annotations.isResourceAnnotation(Object.class))
                .isFalse();
        assertThat(Annotations.isResourceAnnotation(GivenWrongResource.class))
                .isFalse();
        assertThat(Annotations.isResourceAnnotation(WithJacksonMapper.class))
                .isFalse();
    }

    @Test
    @DisplayName("Test is annotation is parser annotation")
    public void testIsParserAnnotation_validParserAnnotation() {
        assertThat(Annotations.isParserAnnotation(WithJacksonMapper.class))
                .isTrue();
        assertThat(Annotations.isParserAnnotation(Object.class))
                .isFalse();
        assertThat(Annotations.isParserAnnotation(WithWrongParser.class))
                .isFalse();
        assertThat(Annotations.isParserAnnotation(GivenBinaryResource.class))
                .isFalse();
    }

    @Resource
    @interface GivenWrongResource {
        String value() default "";
    }

    @Parser
    @interface WithWrongParser {
    }
}
