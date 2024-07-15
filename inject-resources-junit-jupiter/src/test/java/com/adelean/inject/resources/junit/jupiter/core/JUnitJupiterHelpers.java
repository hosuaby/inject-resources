package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.github.zafarkhaja.semver.Version;
import org.junit.jupiter.engine.execution.NamespaceAwareStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JUnitJupiterHelpers {
    public static NamespaceAwareStore store() {
        String junitJupiterVersion = NamespaceAwareStore.class.getPackage().getImplementationVersion();
        Version version = Version.parse(junitJupiterVersion);

        return version.isHigherThan(Version.of(5, 10))
                ? storeJUnit5_10Plus()
                : storeJUnit5_9AndLess();
    }

    private static NamespaceAwareStore storeJUnit5_10Plus() {
        try {
            Class<?> namespacedHierarchicalStoreClass = NamespaceAwareStore.class
                    .getClassLoader()
                    .loadClass("org.junit.platform.engine.support.store.NamespacedHierarchicalStore");

            Object namespacedHierarchicalStore = null;
            for (Constructor<?> constructor : namespacedHierarchicalStoreClass.getConstructors()) {
                if (constructor.getParameterCount() == 1) {
                    namespacedHierarchicalStore = constructor
                            .newInstance(namespacedHierarchicalStoreClass.cast(null));
                }
            }

            return (NamespaceAwareStore) NamespaceAwareStore.class
                    .getConstructors()[0]
                    .newInstance(namespacedHierarchicalStore, InjectionContext.NAMESPACE);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }

    private static NamespaceAwareStore storeJUnit5_9AndLess() {
        try {
            Class<?> extensionValuesStoreClass = NamespaceAwareStore.class
                    .getClassLoader()
                    .loadClass("org.junit.jupiter.engine.execution.ExtensionValuesStore");

            Object extensionValuesStore = null;
            for (Constructor<?> constructor : extensionValuesStoreClass.getConstructors()) {
                if (constructor.getParameterCount() == 1) {
                    extensionValuesStore = constructor
                            .newInstance(extensionValuesStoreClass.cast(null));
                }
            }

            return (NamespaceAwareStore) NamespaceAwareStore.class
                    .getConstructors()[0]
                    .newInstance(extensionValuesStore, InjectionContext.NAMESPACE);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }
}
