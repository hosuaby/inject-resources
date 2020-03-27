package com.adelean.junit.jupiter.resources.core.helpers;

import static io.leangen.geantyref.TypeFactory.parameterizedClass;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollectionFactoryTests {

    @Test
    @DisplayName("Test collections instantiations")
    public void testNewCollection() {

        /* Given */
        var collectionType = (ParameterizedType) parameterizedClass(Collection.class, Object.class);
        var listType = (ParameterizedType) parameterizedClass(List.class, Object.class);
        var navigableSetType = (ParameterizedType) parameterizedClass(NavigableSet.class, Object.class);

        /* When */
        var collection = CollectionFactory.newCollection(collectionType);
        var list = CollectionFactory.newCollection(listType);
        var navigableSet = CollectionFactory.newCollection(navigableSetType);

        /* Then */
        assertThat(collection)
                .isNotNull()
                .isInstanceOf(ArrayList.class);
        assertThat(list)
                .isNotNull()
                .isInstanceOf(ArrayList.class);
        assertThat(navigableSet)
                .isNotNull()
                .isInstanceOf(TreeSet.class);
    }
}
