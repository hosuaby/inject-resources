# @InjectResources

[![CI](https://github.com/hosuaby/inject-resources/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/hosuaby/inject-resources/actions/workflows/ci.yml)
[![Coverage Status](https://coveralls.io/repos/github/hosuaby/inject-resources/badge.svg?branch=master)](https://coveralls.io/github/hosuaby/inject-resources?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.hosuaby/inject-resources-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.hosuaby/inject-resources-core)
[![User guide](https://img.shields.io/badge/User%20guide-0.3.5-red)](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/)
[![Core Javadoc](https://img.shields.io/badge/Core%20Javadoc-0.3.5-orange)](https://javadoc.io/doc/io.hosuaby/inject-resources-core/0.3.5)
[![Spring Javadoc](https://img.shields.io/badge/Spring%20Javadoc-0.3.5-green)](https://javadoc.io/doc/io.hosuaby/inject-resources-spring/0.3.5)
[![JUnit Jupiter Javadoc](https://img.shields.io/badge/JUnit%20Jupiter%20Javadoc-0.3.5-blueviolet)](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-jupiter/0.3.5)
[![JUnit 4 Javadoc](https://img.shields.io/badge/JUnit%204%20Javadoc-0.3.5-yellow)](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-vintage/0.3.5)

*Reading content of resource files in Java is harder that it should be*. Great libraries like
[Guava](https://github.com/google/guava) or [Spring](https://github.com/spring-projects/spring-framework) made it much
easier, but it's time to make another step forward. Use `@InjectResources` is the easiest and most convenient way to load
and parse content of resources without boilerplate code that opens/closes streams and handles I/O exceptions. This library
made of [inject-resources-core](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-core),
fluid Java DSL, and of extensions for
[Spring](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-spring),
[JUnit5](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-jupiter) and
[JUnit4](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-vintage) that allow to do
that with simple annotations.

## Examples

### Core

```java
var text = resource()
        .onClassLoaderOf(this.getClass())
        .withPath("/io/hosuaby/junit/jupiter", "resource.txt")
        .text();
```

Check [Core](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-core) user guide for more information and examples.

### With Spring

```java
@Component
public class MyBean {

    // Field 'text' is injected with content of '/io/hosuaby/junit/jupiter/resource.txt'
    @TextResource("/io/hosuaby/junit/jupiter/resource.txt")
    private String text;
}
```

Check [Spring extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-spring) user guide for more information and examples.

### With JUnit

#### JUnit5

```java
@TestWithResources
class InjectTextResourcesTests {

    @GivenTextResource("/io/hosuaby/junit/jupiter/resource.txt")
    String instanceField;

    @Test
    public void testInjectTextIntoStringInstanceField() {
        assertThat(instanceField)
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
```

Check [JUnit5 extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-jupiter) user guide for more information and examples.

#### JUnit4

```java
class MyTestClass {

    @Rule
    public ResourceRule<String> textResource = givenResource()
            .text("/io/hosuaby/junit/jupiter/resource.txt")
            .withCharset(StandardCharsets.UTF_8);

    @Test
    public void testWithTextResource() {
        assertThat(textResource.get())
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
```

Check [JUnit4 extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-vintage) user guide for more information and examples.

## Supported formats

- Binary
- Text
- Java properties
- JSON & JSON Lines
- YAML & YAML Documents

## News

- 2023-05-28: Release `v0.3.3`. Removed dependency on deprecated InstantiationAwareBeanPostProcessorAdapter.
- 2022-04-07: Release `v0.3.2`. Upgrade `org.reflections` to `0.10.2`.
- 2022-03-23: Release `v0.3.1`. Limit classpath search for `TestsAdvice`s.
- 2021-09-04: Release `v0.3.0`. Fix source compatibility (Gradle). JUnit (5/4) modules now depends on core. 
Implementation no longer relies on classpath scan to find annotations of the project.
- 2021-05-10: Release `v0.2.2`. Use reflexions.org instead of JUnit ReflectionSupport
- 2021-04-23: Release `v0.2.1`. Fix reflexion on annotations on JDK 1.8 
- 2021-03-07: Release `v0.2.0`. Publish to Maven Central.
- 2020-08-13: Release `v0.1.0`. This release includes Spring extension.
- 2020-07-29: Release `v0.1.0-beta`. This release contains support of YAML files (Snakeyaml) and JUnit 4 extension.
- 2020-06-20: Release `v0.1.0-alpha`. The first ever release. Contains only module `core` and JUnit 5 extension.
Support files in formats: binary, text, java properties and JSON.

## Documentation

### User guides
- [Core](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-core),
- [Spring extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-spring),
- [JUnit5 extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-jupiter),
- [JUnit4 extension](https://hosuaby.github.io/inject-resources/0.3.5/asciidoc/#inject-resources-junit-vintage)

### Javadoc

- [Core](https://javadoc.io/doc/io.hosuaby/inject-resources-core/0.3.5),
- [Spring extension](https://javadoc.io/doc/io.hosuaby/inject-resources-spring/0.3.5),
- [JUnit5 extension](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-jupiter/0.3.5),
- [JUnit4 extension](https://javadoc.io/doc/io.hosuaby/inject-resources-junit-vintage/0.3.5)
