package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.core.Parsable;
import com.adelean.inject.resources.junit.jupiter.commons.AnnotationWithName;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import io.leangen.geantyref.TypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.engine.execution.NamespaceAwareStore;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AbstractParserProviderTests {

    @Mock
    ExtensionContext contextMock;

    InjectionContext injectionContext;

    @BeforeEach
    public void init() {
        NamespaceAwareStore store = JUnitJupiterHelpers.store();
        doReturn(store)
                .when(contextMock)
                .getStore(eq(InjectionContext.NAMESPACE));

        injectionContext = new InjectionContext(contextMock);
    }

    @Test
    @DisplayName("Test provide parser from method")
    public void testProvideFromMethod() throws Exception {

        /* Given */
        Method someMethod = SomeClass.class.getDeclaredMethod("someMethod");
        SomeClass someInstance = new SomeClass();

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, someInstance, SomeClass.class);

        /* When */
        parserProvider.provideFromMethod(someMethod, someInstance);

        /* Then */
        Optional<DummyResourceParser> bean = injectionContext.findBean(
                SomeClass.class, "someMethod", DummyResourceParser.class);
        assertThat(bean)
                .isNotNull()
                .isNotEmpty()
                .hasValueSatisfying(parser -> assertThat(parser)
                        .hasFieldOrPropertyWithValue("parser", SomeClass.REF));
    }

    @Test
    @DisplayName("Test provide parser from field")
    public void testProvideFromField() throws Exception {

        /* Given */
        Field someField = SomeClass.class.getDeclaredField("someField");
        SomeClass someInstance = new SomeClass();

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, someInstance, SomeClass.class);

        /* When */
        parserProvider.provideFromField(someField);

        /* Then */
        Optional<DummyResourceParser> bean = injectionContext.findBean(
                SomeClass.class, "someField", DummyResourceParser.class);
        assertThat(bean)
                .isNotNull()
                .isNotEmpty()
                .hasValueSatisfying(parser -> assertThat(parser)
                        .hasFieldOrPropertyWithValue("parser", someInstance.someField));
    }

    @Test
    @DisplayName("Test named parser name from method")
    public void testParserNameFromMethod_namedMapper() throws Exception {

        /* Given */
        AnnotationWithName annotation = TypeFactory.annotation(
                AnnotationWithName.class, singletonMap("name", "some-name"));
        Method someMethod = SomeClass.class.getDeclaredMethod("someMethod");

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, this, AbstractParserProviderTests.class);

        /* When */
        String parserName = parserProvider.parserNameFromMethod(someMethod, annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("some-name");
    }

    @Test
    @DisplayName("Test anonymous parser name from method")
    public void testParserNameFromMethod_anonymousMapper() throws Exception {

        /* Given */
        AnnotationWithName annotation = TypeFactory.annotation(AnnotationWithName.class, emptyMap());
        Method someMethod = SomeClass.class.getDeclaredMethod("someMethod");

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, this, AbstractParserProviderTests.class);

        /* When */
        String parserName = parserProvider.parserNameFromMethod(someMethod, annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("someMethod");
    }

    @Test
    @DisplayName("Test named parser name from field")
    public void testParserNameFromField_namedMapper() throws Exception {

        /* Given */
        AnnotationWithName annotation = TypeFactory.annotation(
                AnnotationWithName.class, singletonMap("name", "some-name"));
        Field someField = SomeClass.class.getDeclaredField("someField");

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, this, AbstractParserProviderTests.class);

        /* When */
        String parserName = parserProvider.parserNameFromField(someField, annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("some-name");
    }

    @Test
    @DisplayName("Test anonymous parser name from field")
    public void testParserNameFromField_anonymousMapper() throws Exception {

        /* Given */
        AnnotationWithName annotation = TypeFactory.annotation(AnnotationWithName.class, emptyMap());
        Field someField = SomeClass.class.getDeclaredField("someField");

        DummyParserProvider parserProvider = new DummyParserProvider(injectionContext, this, AbstractParserProviderTests.class);

        /* When */
        String parserName = parserProvider.parserNameFromField(someField, annotation);

        /* Then */
        assertThat(parserName)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("someField");
    }

    static class SomeClass {
        static final Object REF = new Object();

        @AnnotationWithName
        Object someField = new Object();

        @AnnotationWithName
        Object someMethod() {
            return REF;
        }
    }

    static class DummyParserProvider extends AbstractParserProvider<AnnotationWithName, Object, DummyResourceParser> {
        public DummyParserProvider(
                InjectionContext injectionContext,
                Object testInstance,
                Class<?> testClass) {
            super(injectionContext, testInstance, testClass, AnnotationWithName.class);
        }

        @Override
        protected DummyResourceParser createParser(AnnotationWithName parserAnnotation, Object parser) {
            return new DummyResourceParser(parser);
        }
    }

    static class DummyResourceParser implements ResourceParser<Parsable<Object>, Object> {
        Object parser;

        protected DummyResourceParser(Object parser) {
            this.parser = parser;
        }

        @Override
        public Object parse(Parsable<Object> resource, Type valueType) {
            return new Object();
        }
    }
}
