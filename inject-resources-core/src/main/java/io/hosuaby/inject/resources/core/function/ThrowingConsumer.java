package io.hosuaby.inject.resources.core.function;

/**
 * This interface replaces {@link java.util.function.Consumer} in cases when execution of {@link #accept(Object)} method
 * may throw exception.
 *
 * <p>Useful for capturing lambdas that throw exceptions.</p>
 *
 * @param <T> the type of the input to the operation
 *
 * @author Alexei KLENIN
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t  input argument
     * @throws Throwable exception that may be thrown
     */
    void accept(T t) throws Throwable;
}
