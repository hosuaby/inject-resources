# @InjectResources

[![Build Status](https://travis-ci.org/hosuaby/inject-resources.svg?branch=master)](https://travis-ci.org/hosuaby/inject-resources)
[![Coverage Status](https://coveralls.io/repos/github/hosuaby/inject-resources/badge.svg?branch=master)](https://coveralls.io/github/hosuaby/inject-resources?branch=master)
[![Download](https://api.bintray.com/packages/hosuaby/InjectResources/InjectResources/images/download.svg)](https://bintray.com/hosuaby/InjectResources/InjectResources/_latestVersion)
[![User guide](https://img.shields.io/badge/User%20guide-0.1.0-red)](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/)
[![Core Javadoc](https://img.shields.io/badge/Core%20Javadoc-0.1.0-orange)](https://javadoc.io/doc/com.adelean/inject-resources-core/0.1.0)
[![Spring Javadoc](https://img.shields.io/badge/Spring%20Javadoc-0.1.0-green)](https://javadoc.io/doc/com.adelean/inject-resources-spring/0.1.0)
[![JUnit Jupiter Javadoc](https://img.shields.io/badge/JUnit%20Jupiter%20Javadoc-0.1.0-blueviolet)](https://javadoc.io/doc/com.adelean/inject-resources-junit-jupiter/0.1.0)
[![JUnit 4 Javadoc](https://img.shields.io/badge/JUnit%204%20Javadoc-0.1.0-yellow)](https://javadoc.io/doc/com.adelean/inject-resources-junit-vintage/0.1.0)

*Reading content of resource files in Java is harder that it should be*. Great libraries like
[Guava](https://github.com/google/guava) or [Spring](https://github.com/spring-projects/spring-framework) made it much
easier, but it's time to make another step forward. Use `@InjectResources` is the easiest and most convenient way to load
and parse content of resources without boilerplate code that opens/closes streams and handles I/O exceptions. This library
made of [inject-resources-core](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-core),
fluid Java DSL, and of extensions for
[Spring](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-spring),
[JUnit5](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-junit-jupiter) and
[JUnit4](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-junit-vintage) that allow to do
that with simple annotations.

## Example with Spring

```java
@Component
public class MyBean {

    // Field 'text' is injected with content of '/com/adelean/junit/jupiter/resource.txt'
    @TextResource("/com/adelean/junit/jupiter/resource.txt")
    private String text;
}
```

## News

- 2020-08-13: Release `v0.1.0`. This release includes Spring extension.
- 2020-07-29: Release `v0.1.0-beta`. This release contains support of YAML files (Snakeyaml) and JUnit 4 extension.
- 2020-06-20: Release `v0.1.0-alpha`. The first ever release. Contains only module `core` and JUnit 5 extension.
Support files in formats: binary, text, java properties and JSON.

## Documentation

### User guides
- [Core](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-core),
- [Spring extension](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-spring),
- [JUnit5 extension](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-junit-jupiter),
- [JUnit4 extension](https://hosuaby.github.io/inject-resources/0.1.0/asciidoc/#inject-resources-junit-vintage)

### Javadoc

- [Core](https://javadoc.io/doc/com.adelean/inject-resources-core/0.1.0),
- [Spring extension](https://javadoc.io/doc/com.adelean/inject-resources-spring/0.1.0),
- [JUnit5 extension](https://javadoc.io/doc/com.adelean/inject-resources-junit-jupiter/0.1.0),
- [JUnit4 extension](https://javadoc.io/doc/com.adelean/inject-resources-junit-vintage/0.1.0)
