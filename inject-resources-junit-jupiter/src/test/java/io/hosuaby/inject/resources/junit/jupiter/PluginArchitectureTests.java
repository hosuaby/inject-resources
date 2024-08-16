package io.hosuaby.inject.resources.junit.jupiter;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.junit.jupiter.core.Annotations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public class PluginArchitectureTests {
    private static final Pattern RESOURCE_ANNOTATION_NAME_PATTERN = Pattern.compile("^Given\\w+Resource$");
    private static final Pattern PARSER_ANNOTATION_NAME_PATTERN = Pattern.compile("^With\\w+$");
    private static final String ERR_WRONG_NAME = "%s annotation @%s does not follow the name pattern [%s].";

    @Test
    @DisplayName("Test that resource annotations implemented properly")
    public void testResourceAnnotations() {

        /* Given */
        Collection<Class<? extends Annotation>> allResourceAnnotations = Annotations.RESOURCE_ANNOTATIONS;

        /* When */
        for (Class<? extends Annotation> annotationType : allResourceAnnotations) {

            /* Then */
            assertResourceAnnotationName(annotationType);
            AnnotationSupport.assertInheritProperly(annotationType);
        }
    }

    @Test
    @DisplayName("Test that parser annotations implemented properly")
    public void testParserAnnotations() {

        /* Given */
        Collection<Class<? extends Annotation>> allParserAnnotations = Annotations.PARSER_ANNOTATIONS;

        /* When */
        for (Class<? extends Annotation> annotationType : allParserAnnotations) {

            /* Then */
            assertParserAnnotationName(annotationType);
            AnnotationSupport.assertInheritProperly(annotationType);
        }
    }

    void assertResourceAnnotationName(Class<? extends Annotation> annotationType) {
        boolean matches = RESOURCE_ANNOTATION_NAME_PATTERN.matcher(annotationType.getSimpleName()).matches();
        if (!matches) {
            fail(String.format(ERR_WRONG_NAME,
                    "Resource", annotationType.getSimpleName(), RESOURCE_ANNOTATION_NAME_PATTERN));
        }
    }

    void assertParserAnnotationName(Class<? extends Annotation> annotationType) {
        boolean matches = PARSER_ANNOTATION_NAME_PATTERN.matcher(annotationType.getSimpleName()).matches();
        if (!matches) {
            fail(String.format(ERR_WRONG_NAME,
                    "Parser", annotationType.getSimpleName(), PARSER_ANNOTATION_NAME_PATTERN));
        }
    }
}
