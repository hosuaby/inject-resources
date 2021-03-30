# @InjectResources

[![CI](https://github.com/hosuaby/inject-resources/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/hosuaby/inject-resources/actions/workflows/ci.yml)
[![Coverage Status](https://coveralls.io/repos/github/hosuaby/inject-resources/badge.svg?branch=master)](https://coveralls.io/github/hosuaby/inject-resources?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.hosuaby/inject-resources-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.hosuaby/inject-resources-core)
[![User guide](https://img.shields.io/badge/User%20guide-0.2.0-red)](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/)
[![Core Javadoc](https://img.shields.io/badge/Core%20Javadoc-0.2.0-orange)](https://javadoc.io/doc/io.hosuaby/inject-resources-core/0.2.0)
[![Spring Javadoc](https://img.shields.io/badge/Spring%20Javadoc-0.2.0-green)](https://javadoc.io/doc/io.hosuaby/inject-resources-spring/0.2.0)
[![JUnit Jupiter Javadoc](https://img.shields.io/badge/JUnit%20Jupiter%20Javadoc-0.2.0-blueviolet)](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-jupiter/0.2.0)
[![JUnit 4 Javadoc](https://img.shields.io/badge/JUnit%204%20Javadoc-0.2.0-yellow)](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-vintage/0.2.0)

*Reading content of resource files in Java is harder that it should be*. Great libraries like
[Guava](https://github.com/google/guava) or [Spring](https://github.com/spring-projects/spring-framework) made it much
easier, but it's time to make another step forward. Use `@InjectResources` is the easiest and most convenient way to load
and parse content of resources without boilerplate code that opens/closes streams and handles I/O exceptions. This library
made of [inject-resources-core](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-core),
fluid Java DSL, and of extensions for
[Spring](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-spring),
[JUnit5](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-jupiter) and
[JUnit4](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-vintage) that allow to do
that with simple annotations.

## Examples

### Core

```java
var text = resource()
        .onClassLoaderOf(this.getClass())
        .withPath("/com/adelean/junit/jupiter", "resource.txt")
        .text();
```

Check [Core](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-core) user guide for more information and examples.

### With Spring

```java
@Component
public class MyBean {

    // Field 'text' is injected with content of '/com/adelean/junit/jupiter/resource.txt'
    @TextResource("/com/adelean/junit/jupiter/resource.txt")
    private String text;
}
```

Check [Spring extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-spring) user guide for more information and examples.

### With JUnit

#### JUnit5

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

Check [JUnit5 extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-jupiter) user guide for more information and examples.

#### JUnit4

```java
class MyTestClass {

    @Rule
    public ResourceRule<String> textResource = givenResource()
            .text("/com/adelean/junit/jupiter/resource.txt")
            .withCharset(StandardCharsets.UTF_8);

    @Test
    public void testWithTextResource() {
        assertThat(textResource.get())
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
```

Check [JUnit4 extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-vintage) user guide for more information and examples.

## Supported formats

- Binary
- Text
- Java properties
- JSON & JSON Lines
- YAML & YAML Documents

## News

- 2021-03-07: Release `v0.2.0`. Publish to Maven Central.
- 2020-08-13: Release `v0.1.0`. This release includes Spring extension.
- 2020-07-29: Release `v0.1.0-beta`. This release contains support of YAML files (Snakeyaml) and JUnit 4 extension.
- 2020-06-20: Release `v0.1.0-alpha`. The first ever release. Contains only module `core` and JUnit 5 extension.
Support files in formats: binary, text, java properties and JSON.

## Documentation

### User guides
- [Core](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-core),
- [Spring extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-spring),
- [JUnit5 extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-jupiter),
- [JUnit4 extension](https://hosuaby.github.io/inject-resources/0.2.0/asciidoc/#inject-resources-junit-vintage)

### Javadoc

- [Core](https://javadoc.io/doc/io.hosuaby/inject-resources-core/0.2.0),
- [Spring extension](https://javadoc.io/doc/io.hosuaby/inject-resources-spring/0.2.0),
- [JUnit5 extension](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-jupiter/0.2.0),
- [JUnit4 extension](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-vintage/0.2.0)
