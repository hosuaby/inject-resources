package io.hosuaby.inject.resources.junit.jupiter.core.cdi;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InjectionContext {
    public static final Namespace NAMESPACE = ExtensionContext
            .Namespace
            .create(InjectionContext.class);

    private final Store store;

    public InjectionContext(ExtensionContext context) {
        this.store = context.getStore(NAMESPACE);
    }

    public void defineBean(
            Class<?> testClass,
            String beanName,
            Class<?> beanType,
            Object instance) {
        Context<?> context = store
                .getOrComputeIfAbsent(testClass, Context::new, Context.class);
        Bean<?> bean = new Bean(beanType, beanName, instance);
        context.addBean(bean);
    }

    public <T, B> Optional<B> findBean(Class<T> testClass, String beanName, Class<B> beanType) {
        @SuppressWarnings("unchecked")
        Context<T> context = store.get(testClass, Context.class);

        Optional<B> foundBean = Optional
                .ofNullable(context)
                .flatMap(ctx -> ctx.findBean(beanName, beanType))
                .map(bean -> bean.instance);

        if (!foundBean.isPresent()) {
            Class<? super T> testParentClass = testClass.getSuperclass();
            if (testParentClass != null) {
                foundBean = findBean(testParentClass, beanName, beanType);
            }
        }

        return foundBean;
    }

    private static final class Bean<B> {
        private final Class<B> beanType;
        private final String name;
        private final B instance;

        private Bean(Class<B> beanType, String name, B instance) {
            this.beanType = beanType;
            this.name = name;
            this.instance = instance;
        }

        private boolean matchType(Class<?> requiredType) {
            return this.beanType.isAssignableFrom(requiredType);
        }
    }

    private static final class Context<T> {
        private static final String ERR_PARSER_ALREADY_DEFINED =
                "Class %s has already declared parser with name '%s'.";
        private static final String ERR_BEAN_WRONG_TYPE =
                "Bean %s of type %s declared in test class %s is not casted to %s.";

        private final Class<T> testClass;
        private final Map<String, Bean<?>> beans = new HashMap<>();

        private Context(Class<T> testClass) {
            this.testClass = testClass;
        }

        private <B> void addBean(Bean<B> bean) {
            if (!beans.containsKey(bean.name)) {
                beans.put(bean.name, bean);
            } else {
                throw new RuntimeException(
                        String.format(ERR_PARSER_ALREADY_DEFINED, testClass.getName(), bean.name));
            }
        }

        private <B> Optional<Bean<B>> findBean(String beanName, Class<B> beanType) {
            return beanName != null
                    ? findByName(beanName, beanType)
                    : findByType(beanType);
        }

        @SuppressWarnings("unchecked")
        private <B> Optional<Bean<B>> findByName(String beanName, Class<B> beanType) {
            Bean<?> bean = beans.get(beanName);
            if (bean != null) {
                if (bean.matchType(beanType)) {
                    return Optional.of((Bean<B>) bean);
                } else {
                    throw new RuntimeException(
                            String.format(
                                    ERR_BEAN_WRONG_TYPE,
                                    bean.name,
                                    bean.beanType.getName(),
                                    testClass.getName(),
                                    beanType.getName()));
                }
            } else {
                return Optional.empty();
            }
        }

        @SuppressWarnings("unchecked")
        private <B> Optional<Bean<B>> findByType(Class<B> beanType) {
            return beans
                    .values()
                    .stream()
                    .filter(bean -> bean.matchType(beanType))
                    .map(bean -> (Bean<B>) bean)
                    .findAny();
        }
    }
}
