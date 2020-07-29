# @InjectResources

[![Build Status](https://travis-ci.org/hosuaby/inject-resources.svg?branch=master)](https://travis-ci.org/hosuaby/inject-resources)
[![Coverage Status](https://coveralls.io/repos/github/hosuaby/inject-resources/badge.svg)](https://coveralls.io/github/hosuaby/inject-resources)
[![Download](https://api.bintray.com/packages/hosuaby/InjectResources/InjectResources/images/download.svg)](https://bintray.com/hosuaby/InjectResources/InjectResources/_latestVersion)
[![User guide](https://img.shields.io/badge/User%20guide-0.1.0--beta-red)](https://hosuaby.github.io/inject-resources/0.1.0-beta/asciidoc/)
[![Core Javadoc](https://img.shields.io/badge/Core%20Javadoc-0.1.0--beta-orange)](https://javadoc.io/doc/com.adelean/inject-resources-core)
[![JUnit Jupiter Javadoc](https://img.shields.io/badge/JUnit%20Jupiter%20Javadoc-0.1.0--beta-blueviolet)](https://javadoc.io/doc/com.adelean/inject-resources-junit-jupiter)
[![JUnit 4 Javadoc](https://img.shields.io/badge/JUnit%204%20Javadoc-0.1.0--beta-yellow)](https://javadoc.io/doc/com.adelean/inject-resources-junit-vintage)

A convenient way to have resources content in your tests.

## Example

Binary content of `/com/adelean/junit/jupiter/fibonacci.bin`:

```
1 1 2 3 5 8 13 21 34 55 89
```

Text content of `/com/adelean/junit/jupiter/resource.txt`:

```
The quick brown fox jumps over the lazy dog.
```

```java
import com.adelean.inject.resources.junit.jupiter.GivenBinaryResource;
import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("Tests with injected content of resources")
class MyTests {

    @GivenBinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    byte[] fibonacci;

    @Test
    @DisplayName("Injects binary content into byte[] field")
    public void testInjectBinaryContentIntoByteArrayInstanceField() {
        assertThat(fibonacci)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("Injects text content into String parameter")
    public void testInjectTextContentInStringParameter(
            @GivenTextResource("/com/adelean/junit/jupiter/resource.txt")
            String resourceContent) {
        assertThat(resourceContent)
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
```

## Adding the dependency

Java 8+ is required in order to use this library.

Setup repository (with Maven):

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>central</id>
        <name>bintray</name>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>
```

or with Gradle:

```groovy
repositories {
    jcenter()
}
```

Include library into your project (with Maven):

```xml
<dependency>
    <groupId>com.adelean</groupId>
    <artifactId>inject-resources-core</artifactId>
    <version>0.1.0-beta</version>
    <scope>test</scope>
</dependency>
```

or with Gradle:

```groovy
testCompile group: 'com.adelean', name: 'inject-resources-core', version: '0.1.0-beta'
```

For tests with *JUnit 5* you also need (Maven):

```xml
<dependency>
    <groupId>com.adelean</groupId>
    <artifactId>inject-resources-junit-jupiter</artifactId>
    <version>0.1.0-beta</version>
    <scope>test</scope>
</dependency>
```

or with Gradle:

```groovy
testCompile group: 'com.adelean', name: 'inject-resources-junit-jupiter', version: '0.1.0-beta'
```

## Usage

The following sections explain how to load and parse different types of resources.

### Binary

Given a resource `/com/adelean/junit/jupiter/fibonacci.bin` with content:

```
1 1 2 3 5 8 13 21 34 55 89
```

Import the content of that resource as following:

```java
@TestWithResources
class InjectBinaryResourcesTests {

    @GivenBinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    byte[] fibonacci;

    @Test
    public void testInjectBinaryContentIntoByteArrayInstanceField() {
        assertThat(fibonacci)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
}
```

### Text

Given a text resource `/com/adelean/junit/jupiter/resource.txt` with content:

```
The quick brown fox jumps over the lazy dog.
```

You can inject its content:

```java
@TestWithResources
class InjectTextResourcesTests {

    @GivenTextResource("/com/adelean/junit/jupiter/resource.txt")
    String instanceField;

    @Test
    public void testInjectTextIntoStringInstanceField() {
        assertThat(instanceField)
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
```

### Properties

Given a properties resource `/com/adelean/junit/jupiter/db.properties` with content:

```properties
db.user=hosuaby
db.password=password
db.url=localhost
```

You can use those properties in tests like that:

```java
@TestWithResources
class InjectPropertiesResourcesTests {

    @GivenPropertiesResource("/com/adelean/junit/jupiter/db.properties")
    Properties dbProperties;

    @Test
    public void testInjectTextIntoStringInstanceField() {
        assertThat(dbProperties)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }
}
```

### JSON

`@InjectResources` library simplifies loading and parsing of *JSON* & *JSONL* (JSON Lines) files. Native
support for *Jackson* and *GSON* gives ability to parse *JSON* content into `Map<String, Object>`, `JsonNode` (for *Jackson*) &
`JsonElement` (for *GSON*) or to Java POJO.

In order to parse *JSON* we need to declare a parser:

```java
/* Jackson */
@WithJacksonMapper
ObjectMapper objectMapper = new ObjectMapper();

/* or GSON */
@WithGson
Gson gson = new Gson();
``` 

Following sections explain how to parse *JSON/JSONL* with *Jackson* or *GSON* in details.

#### Jackson

In order to parse resources with *Jackson* both `jackson-core` & `jackson-databind` must be on Classpath.

To specify which `ObjectMapper` is used to parse *JSON* use annotation `@WithJacksonMapper`:

```java
@WithJacksonMapper
ObjectMapper objectMapper = new ObjectMapper();
```

Configure your parser as you need. For example:

```java
@WithJacksonMapper
ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
```

Now you can inject content of *JSON/JSONL* into your tests:

```java
@TestWithResources
class TestsWithJackson {

    @WithJacksonMapper
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    /* JSON */
    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Map<String, Object> jsonAsMap;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    JsonNode jsonNode;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Person spongeBob;

    /* JSONL */
    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Log[] logsAsArray;

    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Collection<Log> logsAsCollection;
}
```

#### GSON

To parse *JSON/JSONL* resources with *GSON*, `com.google.code.gson:gson` must be on Classpath.

Declare `GSON` object used to parse resources and annotate it with `@WithGson`:

```java
@WithGson
Gson gson = new Gson();
```

Now you can inject content of *JSON/JSONL* into your tests:

```java
@TestWithResources
class TestsWithGson {

    @WithGson
    Gson gson = new GsonBuilder();
    
    /* JSON */
    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Map<String, Object> jsonAsMap;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    JsonElement jsonElement;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Person spongeBob;
    
    /* JSONL */
    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Log[] logsAsArray;

    @GivenJsonLinesResource("/com/adelean/junit/jupiter/logs.jsonl")
    Collection<Log> logsAsCollection;
}
```

### YAML

Thanks to native support of *Snakeyaml* library is able to parse *YAML* resources files. `org.yaml:snakeyaml` must be
present on Classpath in order to use that feature.

Declare `Yaml` object used for parsing and annotate it with `@WithSnakeYaml`:

```java
@WithSnakeYaml
Yaml yaml = new Yaml();
```

Resource annotations `@GivenYamlResource` and `@GivenYamlDocumentsResource` can be now used to parse *YAML* with single
or multiple documents respectively:

```java
@TestWithResources
class TestsWithYaml {

    @WithSnakeYaml
    Yaml yaml = new Yaml();

    /* YAML resource with a single document */
    @GivenYamlResource("/com/adelean/junit/jupiter/receipt.yml")
    Map<String, Object> receipt;

    @GivenYamlResource("/com/adelean/junit/jupiter/sponge-bob.yaml")
    Person spongeBob;

    /* YAML resource with multiple documents separated by '---' */
    @GivenYamlDocumentsResource("/com/adelean/junit/jupiter/stacktrace.yaml")
    List<Map<String, Object>> stacktraceAsList;
}
```

`Yaml` object must be configured to be able parse documents from multi-document _YAML_ into POJO:

```java
@TestWithResources
class TestsWithYaml {

    /* Assuming we have defined class Log */
    @WithSnakeYaml("log-parser")
    Yaml logParser = new Yaml(new Constructor(Log.class));

    @GivenYamlDocumentsResource(from = "/com/adelean/junit/jupiter/logs.yml", yaml = "log-parser")
    Log[] logsAsArray;
}
```

### Custom format

There is a wide range of file formats in the computer world and huge number of libraries that know how to load/parse them. `@InjectResources` can not support all those libraries natively. That's why it was choosen to make native integration only with the most used and most stable libraries: *Jackson*, *GSON* and upcoming integration with *SnakeYAML*.

But for all other formats and libraries `@InjectResources` proposes convinient Java DSL to load & parse resources. You only need `inject-resources-core` on Classpath in order to use Java DSL.

Here the example showing how to parse *.properties* file with `jackson-dataformat-properties`:

```java
class JavaDslTests {
    static final JavaPropsSchema SCHEMA = JavaPropsSchema
            .emptySchema()
            .withoutPathSeparator();
    static final ObjectReader READER = new JavaPropsMapper()
            .readerFor(DbConnection.class)
            .with(SCHEMA);
    static final String PATH_PREFIX = "/com/adelean/junit/jupiter";
    
    static class DbConnection {
        @JsonProperty("db.user") String user;
        @JsonProperty("db.password") String password;
        @JsonProperty("db.url") String url;
    }
    
    @Test
    public void testResourceAsStream() {
        var dbConnection = resource()
                .withPath(PATH_PREFIX, "db.properties")
                .asInputStream()
                .parseChecked(READER::readValue);

        assertThat(dbConnection)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user", "hosuaby")
                .hasFieldOrPropertyWithValue("password", "password")
                .hasFieldOrPropertyWithValue("url", "localhost");
    }
}
```

### Parser scopes

This section talks about the scope of objects annotated with `@With*`. Those objects are called **parsers**. Parsers can be named or anonymous. Named parsers are useful when some resources require a special configuration of parser. For example:

```java
@TestWithResources
class TestsWithNamedParser {

    @WithJacksonMapper("custom-mapper")
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    @GivenJsonLinesResource(
        from = "/com/adelean/junit/jupiter/logs.jsonl",
        jacksonMapper = "custom-mapper")
    Collection<Log> logsAsCollection;
}
```

In this example, resource `/com/adelean/junit/jupiter/logs.jsonl` is parsed by `ObjectMapper` named *"custom-mapper"*.

By default, parsers are scoped to test class where they were defined. If a test class inherit from another class, parsers declared in super-class can be used in subclass:

```java

abstract class SuperClassWithParser {

    @WithJacksonMapper("custom-mapper")
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
}

@TestWithResources
class TestsSubclass extends SuperClassWithParser {

    @GivenJsonLinesResource(
        from = "/com/adelean/junit/jupiter/logs.jsonl",
        jacksonMapper = "custom-mapper")
    Collection<Log> logsAsCollection;
}

```

#### Tests advice

It is possible to define a parser that can be used by all tests on classpath by creating a `public` `final` class annotated with `@TestsAdvice`:

```java
@TestsAdvice
public final class GlobalJacksonMapper {

    @WithJacksonMapper("custom-mapper")
    ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}

@TestWithResources
class TestsWithJson {

    @GivenJsonLinesResource(
        from = "/com/adelean/junit/jupiter/logs.jsonl",
        jacksonMapper = "custom-mapper")
    Collection<Log> logsAsCollection;
}
```

## What's coming next

- [ ] XML support
- [ ] Caching for parsed resources
- [ ] Spring support
- [ ] CSV support

