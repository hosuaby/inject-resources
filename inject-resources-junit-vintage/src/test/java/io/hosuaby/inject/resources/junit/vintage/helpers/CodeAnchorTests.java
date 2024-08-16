package io.hosuaby.inject.resources.junit.vintage.helpers;

import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class CodeAnchorTests {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CodeAnchor.class).verify();
    }
}
