package com.adelean.junit.jupiter.resources.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
