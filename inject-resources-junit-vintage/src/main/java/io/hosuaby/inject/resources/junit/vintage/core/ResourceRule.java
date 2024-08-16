package io.hosuaby.inject.resources.junit.vintage.core;

import org.junit.rules.TestRule;

/**
 * Interface implemented by all resource rules.
 * @param <T>  type of resource content
 *
 * @author Alexei KLENIN
 */
public interface ResourceRule<T> extends TestRule {

    /**
     * Returns content of represented resource converted to class {@code T}. This method can only be used from test
     * method. Usage from other places is not supported.
     *
     * <p>Example:
     *
     * <pre>
     * public class TestsWithResource {
     *
     *     &#64;Rule
     *     public TextResource textResource = givenResource()
     *             .text("/io/hosuaby/resource.txt");
     *
     *     &#64;Test
     *     public void testWithResourceContent() {
     *         var textContent = textResource.get();
     *         // ...
     *     }
     * }
     * </pre>
     *
     * @return content of resource
     */
    T get();
}
