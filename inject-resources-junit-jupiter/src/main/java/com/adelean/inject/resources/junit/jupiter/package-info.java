/**
 * This package provides exposed API of {@link com.adelean.inject.resources.junit.jupiter.TestWithResources} JUnit
 * Jupiter extension.
 *
 * <p>This API consists of four parts:</p>
 * <ul>
 *     <li>@{@link com.adelean.inject.resources.junit.jupiter.TestWithResources} - adds this extension to a test
 *     class.</li>
 *     <li>Annotations {@code @Given*Resource} - a.k.a resource annotations. Request content of resource.</li>
 *     <li>Annotations {@code @With*} - a.k.a parser annotations. Declared parser objects.</li>
 *     <li>@{@link com.adelean.inject.resources.junit.jupiter.TestsAdvice} - defines Tests Advice, a class that
 *     provides globally available parsers.</li>
 * </ul>
 *
 * <h2>Rules of parser selection</h2>
 * <p>Parsers are created in execution contexts of tests by {@code @With*} annotations. Parsers can be named or
 * anonymous. All of {@code @With*} annotations have attribute {@code name} to specify the name of parser. Annotations
 * {@code @Given*Resource} that relies on parsers have parameter to specify the name of parser.</p>
 *
 * <p>When {@code @Given*Resource} requires a parser to perform resource parsing and injection, it starts to look up
 * for a parser following those rule:</p>
 *
 * <ul>
 *     <li>If resource annotation requires named parser, we are looking for the parser with that name and right type
 *     defined in the same test class.</li>
 *     <li>If we don't found the parser with requested name in the same test class, we start to look up for a parser
 *     with that name in parent classes starting from immediate super class.</li>
 *     <li>If named parser was not found in any of test class super classes, we look inside a class annotated with
 *     {@code @TestsAdvice}.</li>
 *     <li>If named parser still was not found. Exception is thrown.</li>
 *     <li>If resource annotation do not specify the name of parser, we are first looking for any parser with a right
 *     type first in the same test class, and after, if it was not found, in parent classes starting by immediate super
 *     class.</li>
 *     <li>If none of test class super classes declare required parser, we check a class annotated with
 *     {@code @TestsAdvice}.</li>
 *     <li>For some kind of resources, if no parser was found, we will use the default instance of parser. In other
 *     cases we will throw an exception. Check the javadoc of particular resource annotation to understand its
 *     behaviour.</li>
 * </ul>
 *
 * @author Alexei KLENIN
 */
package com.adelean.inject.resources.junit.jupiter;
