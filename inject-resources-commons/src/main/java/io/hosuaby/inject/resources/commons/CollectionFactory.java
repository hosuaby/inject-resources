package io.hosuaby.inject.resources.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
 * interfaces. Inspired by {@code BasicDeserializerFactory.ContainerDefaultMappings} of Jackson.
 */
@SuppressWarnings("rawtypes")
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
        @SuppressWarnings("unchecked")
        Class<? extends Collection> collectionClass = (Class<? extends Collection>) collectionType.getRawType();

        if (Modifier.isAbstract(collectionClass.getModifiers()) || collectionClass.isInterface()) {
            collectionClass = COLLECTION_FALLBACKS.get(collectionClass);
        }

        try {
            Constructor<? extends Collection> constructor = collectionClass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException collectionInstantiationException) {
            throw new RuntimeException(collectionInstantiationException);
        }
    }
}
