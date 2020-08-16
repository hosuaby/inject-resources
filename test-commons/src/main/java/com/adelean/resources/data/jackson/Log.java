package com.adelean.resources.data.jackson;

import java.time.LocalDateTime;
import java.util.Objects;

import com.adelean.resources.data.LogSeverity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Log {
    LocalDateTime timestamp;
    LogSeverity severity;
    String message;

    public Log(
            @JsonProperty("date")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime timestamp,
            @JsonProperty("severity")
            LogSeverity severity,
            @JsonProperty("msg")
            String message) {
        this.timestamp = timestamp;
        this.severity = severity;
        this.message = message;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Log another = (Log) other;
        return Objects.equals(timestamp, another.timestamp)
                && severity == another.severity
                && Objects.equals(message, another.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, severity, message);
    }
}
