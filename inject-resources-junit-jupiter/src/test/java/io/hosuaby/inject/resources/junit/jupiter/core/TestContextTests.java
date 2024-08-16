package io.hosuaby.inject.resources.junit.jupiter.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestContextTests {

    @Mock
    ExtensionContext contextMock;

    @Test
    @DisplayName("Test name of package to scan for Advice, when package of tested class has dept of more than 3")
    public void testPackageForAdviceScan_longTestedClassPackageName() {

        /* Given */
        var testContext = TestContext.ofClass(TestContextTests.class, contextMock);

        /* When */
        var packageForAdviceScan = testContext.packageForAdviceScan();

        /* Then */
        assertThat(packageForAdviceScan)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("io.hosuaby.inject");
    }

    @Test
    @DisplayName("Test name of package to scan for Advice, when package of tested class has dept of less than 3")
    public void testPackageForAdviceScan_shortTestedClassPackageName() {

        /* Given */
        var testContext = TestContext.ofClass(String.class, contextMock);

        /* When */
        var packageForAdviceScan = testContext.packageForAdviceScan();

        /* Then */
        assertThat(packageForAdviceScan)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("java.lang");
    }
}
