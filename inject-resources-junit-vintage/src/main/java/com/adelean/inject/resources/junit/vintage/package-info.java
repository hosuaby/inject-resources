/**
 * This package provides exposed API of {@link com.adelean.inject.resources.junit.vintage.GivenResource} JUnit4
 * (Vintage) extension.
 *
 * <p>This extension consist of set of JUnit rules that can be used to get content of resources in tests easily.
 *
 * <h2>Available resource rules:</h2>
 * <ul>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.binary.BinaryResource} - rule for resources with binary
 *     content</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.text.TextResource} - rule for resources with text
 *     content</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.properties.PropertiesResource} - rule for resources
 *     with java properties</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.json.JsonResource} - rule for resources with JSON
 *     content</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.json.JsonLinesResource} - rule for resources in format
 *     JSON Lines (one JSON document per line)</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.yaml.YamlResource} - rule for resource with YAML
 *     content</li>
 *     <li>{@link com.adelean.inject.resources.junit.vintage.yaml.YamlDocumentsResource} - rule for resources with
 *     multiple YAML documents within a single file</li>
 * </ul>
 *
 * <p>All presented rules can be created via factory method
 * {@link com.adelean.inject.resources.junit.vintage.GivenResource#givenResource()}. It's recommended to import this
 * method statically:
 *
 * <pre>{@code
 * import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
 * }</pre>
 *
 * <p>After that, all rules can be created via chained builders:
 *
 * <pre>
 * &#64;Rule
 * public JsonResource&#60;Person&#62; spongeBob = givenResource()
 *         .json("/com/adelean/junit/jupiter/sponge-bob.json")
 *         .withCharset(StandardCharsets.UTF_8)
 *         .parseWith(new ObjectMapper());
 * </pre>
 *
 * <p>To get the content of resource, need to call method {@code get()} on the rule object:
 *
 * <pre>
 * &#64;Test
 * public void testWithContentOfJsonResource() {
 *     var actual = spongeBob.get();
 *     // ...
 * }
 * </pre>
 *
 * @see com.adelean.inject.resources.junit.vintage.GivenResource
 * @author Alexei KLENIN
 */
package com.adelean.inject.resources.junit.vintage;
