package io.hosuaby.inject.resources.commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.leangen.geantyref.TypeFactory;

public class ClassSupportTests {

    @Test
    @DisplayName("Test is subclass")
    public void testIsSubclass() {
        assertThat(ClassSupport.isSubclass(ArrayList.class, Object.class))
                .isTrue();
        assertThat(ClassSupport.isSubclass(ArrayList.class, Collection.class))
                .isTrue();
        assertThat(ClassSupport.isSubclass(Object.class, ArrayList.class))
                .isFalse();
        assertThat(ClassSupport.isSubclass(Collection.class, ArrayList.class))
                .isFalse();
        assertThat(ClassSupport.isSubclass(ArrayList.class, ArrayList.class))
                .isFalse();
    }

    @Test
    @DisplayName("Test is array")
    public void testIsArray() {

        /* Given */
        var byteArrayType = TypeFactory.parameterizedClass(byte[].class);
        var stringArrayType = TypeFactory.parameterizedClass(String[].class);
        var genericArrayType = TypeFactory.arrayOf(
                TypeFactory.parameterizedClass(
                        Map.class, String.class, Object.class));
        var arrayListType = TypeFactory.parameterizedClass(ArrayList.class, Object.class);
        var objectType = TypeFactory.parameterizedClass(Object.class);

        /* When */
        // NOTHING

        /* Then */
        assertThat(ClassSupport.isArray(byteArrayType))
                .isTrue();
        assertThat(ClassSupport.isArray(stringArrayType))
                .isTrue();
        assertThat(ClassSupport.isArray(genericArrayType))
                .isTrue();
        assertThat(ClassSupport.isArray(arrayListType))
                .isFalse();
        assertThat(ClassSupport.isArray(objectType))
                .isFalse();
    }

    @Test
    @DisplayName("Test is collection")
    public void testIsCollection() {

        /* Given */
        var arrayListType = TypeFactory.parameterizedClass(ArrayList.class, Object.class);
        var stringSetType = TypeFactory.parameterizedClass(Set.class, String.class);
        var stringArrayType = TypeFactory.parameterizedClass(String[].class);

        /* When */
        // NOTHING

        /* Then */
        assertThat(ClassSupport.isCollection(ArrayList.class))
                .isTrue();
        assertThat(ClassSupport.isCollection(arrayListType))
                .isTrue();
        assertThat(ClassSupport.isCollection(stringSetType))
                .isTrue();
        assertThat(ClassSupport.isCollection(stringArrayType))
                .isFalse();
    }

    @Test
    @DisplayName("Test get element type")
    public void testElementType() {

        /* Given */
        var byteArrayType = TypeFactory.parameterizedClass(byte[].class);
        var stringArrayType = TypeFactory.parameterizedClass(String[].class);
        var arrayListType = TypeFactory.parameterizedClass(ArrayList.class, Long.class);
        var objectType = TypeFactory.parameterizedClass(Object.class);

        /* When */
        // NOTHING

        /* Then */
        assertThat(ClassSupport.elementType(byteArrayType))
                .isNotNull()
                .isEqualTo(byte.class);
        assertThat(ClassSupport.elementType(stringArrayType))
                .isNotNull()
                .isEqualTo(String.class);
        assertThat(ClassSupport.elementType(arrayListType))
                .isNotNull()
                .isEqualTo(Long.class);
        assertThat(ClassSupport.elementType(objectType))
                .isNull();
    }
}
