package io.hosuaby.inject.resources.core.function;

/**
 * This interface replaces {@link java.util.function.Function} in cases when execution of {@link #apply(Object)} method
 * may throw exception.
 *
 * <p>Useful for capturing lambdas that throw exceptions.</p>
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 *
 * @author Alexei KLENIN
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t  function argument
     * @return function result
     * @throws Throwable exception that may be thrown
     */
    R apply(T t) throws Throwable;
}
