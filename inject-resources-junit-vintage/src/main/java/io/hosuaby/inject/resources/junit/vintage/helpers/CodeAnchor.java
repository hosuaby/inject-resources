package io.hosuaby.inject.resources.junit.vintage.helpers;

import java.util.Objects;

public final class CodeAnchor {
    private final String className;
    private final int lineNumber;

    public CodeAnchor(StackTraceElement stackTraceElement) {
        this(stackTraceElement.getClassName(), stackTraceElement.getLineNumber());
    }

    CodeAnchor(String className, int lineNumber) {
        this.className = className;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CodeAnchor)) {
            return false;
        }

        CodeAnchor that = (CodeAnchor) other;
        return lineNumber == that.lineNumber
                && Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, lineNumber);
    }
}
