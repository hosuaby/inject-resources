package io.hosuaby.inject.resources.core;

/**
 * {@code InjectResources} is a small fluid Java DSL that simplifies loading and parsing of resource files.
 *
 * <p>This library takes care of opening/closing of streams and handles exceptions.</p>
 *
 * <p>Example:</p>
 *
 * <pre>
 * String text = resource()
 *      .withPath("/io/hosuaby", "resource.txt")
 *      .asText()
 *      .text();
 * </pre>
 *
 * <p>Or with resources content parsing:</p>
 *
 * <pre>
 * // Assuming there is object 'reader' able to parse properties content in variable of type DbConnection
 * DbConnection dbConnection = resource()
 *      .withPath("/io/hosuaby", "db.properties")
 *      .asInputStream()
 *      .parseChecked(reader::readValue);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class InjectResources {
    private InjectResources() {
    }

    /**
     * Entry point of {@code InjectResources}s fluid Java DSL.
     *
     * @return {@code InjectResources} resource factory
     */
    public static InjectResources resource() {
        return new InjectResources();
    }

    /**
     * Returns builder for resource that is present on a classpath of {@link ClassLoader} of {@code clazz} argument.
     *
     * @param clazz  class on a classpath where requested resource is present
     * @return {@link Resource.ResourceOnClassloader} resource factory
     */
    public Resource.ResourceOnClassloader onClassLoaderOf(Class<?> clazz) {
        return onClassLoader(clazz.getClassLoader());
    }

    /**
     * Returns builder for resource that is present on a classpath of {@code classLoader} argument.
     *
     * @param classLoader  classloader from a classpath where requested resource is present
     * @return {@link Resource.ResourceOnClassloader} resource factory
     */
    public Resource.ResourceOnClassloader onClassLoader(ClassLoader classLoader) {
        return new Resource.ResourceOnClassloader(classLoader);
    }

    /**
     * Returns builder for resource with defined {@code path} on a classpath of the threads context {@link ClassLoader}.
     *
     * <p>Those two operations are equivalent:</p>
     *
     * <pre>
     *     withPath("/io/hosuaby/resource.txt")
     *
     *     // equivalent to
     *
     *     withPath("/io/hosuaby", "resource.txt")
     * </pre>
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @return {@link Resource} resource builder
     */
    public Resource withPath(String firstPathToken, String... otherTokens) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return onClassLoader(contextClassLoader).withPath(firstPathToken, otherTokens);
    }
}
