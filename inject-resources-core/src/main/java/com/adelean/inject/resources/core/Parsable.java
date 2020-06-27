package com.adelean.inject.resources.core;

import com.adelean.inject.resources.core.function.ThrowingConsumer;
import com.adelean.inject.resources.core.function.ThrowingFunction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface of resource builder that can parse content of resource.
 *
 * @param <I>  type of resource content
 *
 * @author Alexei KLENIN
 */
public interface Parsable<I> extends Readable<I> {

    /**
     * Parses content of resource using {@code parsingFunction}.
     *
     * @param parsingFunction  parsing function
     * @param <O>  type of parsed content
     * @return parsed resource content
     */
    default <O> O parse(Function<I, O> parsingFunction) {
        return parsingFunction.apply(read());
    }

    /**
     * Parses content of resource using {@code parsingFunction} that may throw exception.
     *
     * <p>Function converts all thrown checked exceptions to unchecked {@link RuntimeException}.</p>
     *
     * @param parsingFunction  parsing function that may throw exception
     * @param <O>  type of parsed content
     * @return parsed resource content
     */
    default <O> O parseChecked(ThrowingFunction<I, O> parsingFunction) {
        I content = read();

        try {
            return parsingFunction.apply(content);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            if (content instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) content).close();
                } catch (Exception closeException) {
                    throw new RuntimeException(closeException);
                }
            }
        }
    }

    /**
     * Processes content of resource using {@code contentConsumer}.
     *
     * @param contentConsumer  content consumer
     */
    default void then(Consumer<I> contentConsumer) {
        contentConsumer.accept(read());
    }

    /**
     * Processes content of resource using {@code contentConsumer} that may throw exception.
     *
     * @param contentConsumer  content consumer that may throw exception
     */
    default void thenChecked(ThrowingConsumer<I> contentConsumer) {
        I content = read();

        try {
            contentConsumer.accept(read());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            if (content instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) content).close();
                } catch (Exception closeException) {
                    throw new RuntimeException(closeException);
                }
            }
        }
    }
}
