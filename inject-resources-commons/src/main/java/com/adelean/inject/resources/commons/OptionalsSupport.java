package com.adelean.inject.resources.commons;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utils to deal with {@link java.util.Optional}s.
 *
 * @see <a href="https://stackoverflow.com/a/47532616">https://stackoverflow.com/a/47532616</a>
 */
public class OptionalsSupport {

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> firstPresent(Supplier<Optional<? extends T>>... optionals) {
        return (Optional<T>) Stream
                .of(optionals)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(firstFound -> (Optional<Optional<T>>) firstFound);
    }
}
