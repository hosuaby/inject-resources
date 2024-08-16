package io.hosuaby.inject.resources.spring;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import io.hosuaby.inject.resources.annotations.Resource;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

public class PluginArchitectureTests {

    @Test
    @DisplayName("Resource annotations must have *Resource suffix")
    public void testResourceAnnotationNames() {
        var importedClasses = new ClassFileImporter().importPackagesOf(EnableResourceInjection.class);

        var resourceAnnotationsRule = classes()
                .that().areAnnotatedWith(Resource.class)
                .should().beAssignableTo(Annotation.class)
                .andShould().haveSimpleNameEndingWith("Resource");

        resourceAnnotationsRule.check(importedClasses);
    }
}
