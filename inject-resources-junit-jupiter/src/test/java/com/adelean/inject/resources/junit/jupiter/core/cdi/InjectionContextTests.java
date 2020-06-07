package com.adelean.inject.resources.junit.jupiter.core.cdi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.engine.execution.ExtensionValuesStore;
import org.junit.jupiter.engine.execution.NamespaceAwareStore;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;

@DisplayName("Test InjectionContext")
@ExtendWith(MockitoExtension.class)
public class InjectionContextTests {

    @Mock
    ExtensionContext contextMock;

    InjectionContext injectionContext;

    @BeforeEach
    public void init() {
        ExtensionValuesStore extensionValuesStore = new ExtensionValuesStore(null);
        NamespaceAwareStore store = new NamespaceAwareStore(extensionValuesStore, InjectionContext.NAMESPACE);

        doReturn(store)
                .when(contextMock)
                .getStore(eq(InjectionContext.NAMESPACE));

        injectionContext = new InjectionContext(contextMock);
    }

    @Test
    @DisplayName("injects anonymous beans from local context")
    public void testInjectAnonymousBean_fromLocalContext() {

        /* Given */
        Object parentBean = new Object();
        Object childBean = new Object();

        injectionContext.defineBean(ParentTest.class, "bean", Object.class, parentBean);
        injectionContext.defineBean(ChildTest.class, "bean", Object.class, childBean);

        /* When */
        Optional<Object> beanInParentTest = injectionContext.findBean(ParentTest.class, null, Object.class);
        Optional<Object> beanInChildTest = injectionContext.findBean(ChildTest.class, null, Object.class);

        /* Then */
        assertThat(beanInParentTest)
                .isNotNull()
                .isNotEmpty()
                .contains(parentBean);

        assertThat(beanInChildTest)
                .isNotNull()
                .isNotEmpty()
                .contains(childBean);
    }

    @Test
    @DisplayName("injects anonymous beans from parent context")
    public void testInjectAnonymousBean_fromParentContext() {

        /* Given */
        Object bean = new Object();
        injectionContext.defineBean(ParentTest.class, "bean", Object.class, bean);

        /* When */
        Optional<Object> beanInChildTest = injectionContext.findBean(ChildTest.class, null, Object.class);

        /* Then */
        assertThat(beanInChildTest)
                .isNotNull()
                .isNotEmpty()
                .contains(bean);
    }

    @Test
    @DisplayName("do not inject anonymous beans from parent context")
    public void testInjectAnonymousBean_fromChildContext() {

        /* Given */
        Object bean = new Object();
        injectionContext.defineBean(ChildTest.class, "bean", Object.class, bean);

        /* When */
        Optional<Object> beanInParentTest = injectionContext.findBean(ParentTest.class, "bean", Object.class);

        /* Then */
        assertThat(beanInParentTest)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("injects named beans from local context")
    public void testInjectNamedBean_fromLocalContext() {

        /* Given */
        Object parentBean = new Object();
        Object childBean = new Object();

        injectionContext.defineBean(ParentTest.class, "bean", Object.class, parentBean);
        injectionContext.defineBean(ChildTest.class, "bean", Object.class, childBean);

        /* When */
        Optional<Object> beanInParentTest = injectionContext.findBean(ParentTest.class, "bean", Object.class);
        Optional<Object> beanInChildTest = injectionContext.findBean(ChildTest.class, "bean", Object.class);

        /* Then */
        assertThat(beanInParentTest)
                .isNotNull()
                .isNotEmpty()
                .contains(parentBean);

        assertThat(beanInChildTest)
                .isNotNull()
                .isNotEmpty()
                .contains(childBean);
    }

    @Test
    @DisplayName("injects named beans from parent context")
    public void testInjectNamedBean_fromParentContext() {

        /* Given */
        Object bean = new Object();
        injectionContext.defineBean(ParentTest.class, "bean", Object.class, bean);

        /* When */
        Optional<Object> beanInChildTest = injectionContext.findBean(ChildTest.class, "bean", Object.class);

        /* Then */
        assertThat(beanInChildTest)
                .isNotNull()
                .isNotEmpty()
                .contains(bean);
    }

    @Test
    @DisplayName("do not inject named beans from parent context")
    public void testInjectNamedBean_fromChildContext() {

        /* Given */
        Object bean = new Object();
        injectionContext.defineBean(ChildTest.class, "bean", Object.class, bean);

        /* When */
        Optional<Object> beanInParentTest = injectionContext.findBean(ParentTest.class, "bean", Object.class);

        /* Then */
        assertThat(beanInParentTest)
                .isNotNull()
                .isEmpty();
    }

    static class ParentTest {
    }

    static class ChildTest extends ParentTest {
    }
}
