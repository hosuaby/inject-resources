package com.adelean.inject.resources.junit.jupiter.core.helpers;

import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;

import java.lang.reflect.ParameterizedType;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Factory for {@link Collection}s. Provides default implementation for all abstract collections and collection
 * interfaces. Inspired by {@link BasicDeserializerFactory.ContainerDefaultMappings}.
 */
public final class CollectionFactory {
    private static final Class<? extends Collection> DEFAULT_LIST = ArrayList.class;
    private static final Class<? extends Collection> DEFAULT_SET = HashSet.class;

    private static final Map<Class<? extends Collection>, Class<? extends Collection>> COLLECTION_FALLBACKS =
            new HashMap<>();
    static {
        COLLECTION_FALLBACKS.put(Collection.class, DEFAULT_LIST);
        COLLECTION_FALLBACKS.put(List.class, DEFAULT_LIST);
        COLLECTION_FALLBACKS.put(Set.class, DEFAULT_SET);
        COLLECTION_FALLBACKS.put(SortedSet.class, TreeSet.class);
        COLLECTION_FALLBACKS.put(Queue.class, LinkedList.class);
        COLLECTION_FALLBACKS.put(AbstractList.class, DEFAULT_LIST);
        COLLECTION_FALLBACKS.put(AbstractSet.class, DEFAULT_SET);
        COLLECTION_FALLBACKS.put(Deque.class, LinkedList.class);
        COLLECTION_FALLBACKS.put(NavigableSet.class, TreeSet.class);
    }

    private CollectionFactory() {
    }

    public static Collection newCollection(ParameterizedType collectionType) {
        Class<? extends Collection> collectionClass = (Class<? extends Collection>) collectionType.getRawType();

        if (ModifierSupport.isAbstract(collectionClass) || collectionClass.isInterface()) {
            collectionClass = COLLECTION_FALLBACKS.get(collectionClass);
        }

        return ReflectionSupport.newInstance(collectionClass);
    }
}
