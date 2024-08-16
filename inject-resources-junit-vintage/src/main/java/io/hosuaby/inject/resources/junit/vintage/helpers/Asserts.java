package io.hosuaby.inject.resources.junit.vintage.helpers;

import static io.hosuaby.inject.resources.commons.ClassSupport.isArray;
import static io.hosuaby.inject.resources.commons.ClassSupport.isCollection;

import io.hosuaby.inject.resources.junit.vintage.core.ResourceRule;

import java.lang.reflect.Type;

public final class Asserts {
    private static final String ERR_TYPE_ARGUMENT_NOT_ARRAY_OR_COLLECTION =
            "Type argument of rule %s must be array or collection, but was %s.";

    private Asserts() {
    }

    public static Type assertArrayOrCollection(Type targetType, Class<? extends ResourceRule> resourceRuleClass) {
        boolean valid = isArray(targetType) || isCollection(targetType);

        if (!valid) {
            throw new IllegalArgumentException(String.format(
                    ERR_TYPE_ARGUMENT_NOT_ARRAY_OR_COLLECTION,
                    resourceRuleClass.getSimpleName(),
                    targetType.getTypeName()));
        }

        return targetType;
    }
}
