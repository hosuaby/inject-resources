package com.adelean.inject.resources.junit.vintage;

import com.adelean.inject.resources.junit.vintage.binary.BinaryResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import com.adelean.inject.resources.junit.vintage.json.JsonLinesResource;
import com.adelean.inject.resources.junit.vintage.json.JsonResource;
import com.adelean.inject.resources.junit.vintage.properties.PropertiesResource;
import com.adelean.inject.resources.junit.vintage.text.TextResource;
import com.adelean.inject.resources.junit.vintage.yaml.YamlDocumentsResource;
import com.adelean.inject.resources.junit.vintage.yaml.YamlResource;

/**
 * Entry point of {@code inject-resources} JUnit4 (Vintage) extension.
 *
 * <p>The recommended usage is to import method {@link #givenResource()} statically in the test class and use it as
 * chained builder for resource rules.
 *
 * <pre>{@code
 * import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
 * }</pre>
 *
 * <p>Then
 *
 * <pre>
 * &#64;Rule
 * public JsonResource&#60;Person&#62; spongeBob = givenResource()
 *         .json("/com/adelean/junit/jupiter/sponge-bob.json")
 *         .withCharset(StandardCharsets.UTF_8)
 *         .parseWith(new ObjectMapper());
 * </pre>
 *
 * @see BinaryResource
 * @see TextResource
 * @see PropertiesResource
 * @see JsonResource
 * @see JsonLinesResource
 * @see YamlResource
 * @see YamlDocumentsResource
 * @author Alexei KLENIN
 */
public final class GivenResource {
    final CodeAnchor codeAnchor;

    private GivenResource(CodeAnchor codeAnchor) {
        this.codeAnchor = codeAnchor;
    }

    /**
     * Entry point of API. It's recommended to import this method statically in test class.
     *
     * <pre>{@code
     * import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
     * }</pre>
     *
     * @return resource rule builder entry point
     */
    public static GivenResource givenResource() {
        StackTraceElement callerTrace = Thread.currentThread().getStackTrace()[2];
        CodeAnchor codeAnchor = new CodeAnchor(callerTrace);
        return new GivenResource(codeAnchor);
    }

    /**
     * Creates {@link BinaryResource} rule for loading of content of binary resources.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @return rule for loading of content of binary resources
     * @see BinaryResource
     */
    public BinaryResource binary(String firstPathToken, String... otherTokens) {
        return new BinaryResource(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link TextResource} rule for loading of content of text resources.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @return rule for loading of content of text resources
     * @see TextResource
     */
    public TextResource text(String firstPathToken, String... otherTokens) {
        return new TextResource(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link PropertiesResource} rule for loading of content of java properties resources.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @return rule for loading of content of java properties resources
     * @see PropertiesResource
     */
    public PropertiesResource properties(String firstPathToken, String... otherTokens) {
        return new PropertiesResource(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link JsonResource} rule for loading and parsing of JSON resources.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @param <T>  type to which content of JSON resource will be converted
     * @return rule for loading and parsing of JSON resources
     * @see JsonResource
     */
    public <T> JsonResource<T> json(String firstPathToken, String... otherTokens) {
        return new JsonResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link JsonLinesResource} rule for loading and parsing of resources in format JSON Lines (one JSON
     * document) per line.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @param <T>  type to which each document from JSON Lines resource must be converted
     * @return rule for loading and parsing of resources in format JSON Lines
     * @see JsonLinesResource
     */
    public <T> JsonLinesResource<T> jsonLines(String firstPathToken, String... otherTokens) {
        return new JsonLinesResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link YamlResource} rule for loading and parsing of YAML resources.
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @param <T>  type to which content of YAML resource will be converted
     * @return rule for loading and parsing of YAML resources
     * @see YamlResource
     */
    public <T> YamlResource<T> yaml(String firstPathToken, String... otherTokens) {
        return new YamlResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    /**
     * Creates {@link YamlDocumentsResource} rule for loading and parsing of YAML resources containing multiple
     * documents separated by three hyphens "---".
     *
     * @param firstPathToken  path of resource, it least one token
     * @param otherTokens  path of resource, allows multiple tokens
     * @param <T>  type to which each document from multi-document YAML resource will be converted
     * @return rule for loading and parsing of YAML resources containing multiple documents
     * @see YamlDocumentsResource
     */
    public <T>YamlDocumentsResource<T> yamlDocuments(String firstPathToken, String... otherTokens) {
        return new YamlDocumentsResource<>(codeAnchor, firstPathToken, otherTokens);
    }
}
