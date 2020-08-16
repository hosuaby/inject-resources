package com.adelean.resources.data.gson;

import com.adelean.resources.data.LogSeverity;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Objects;

public class Log {

    @SerializedName("date")
    LocalDateTime timestamp;

    @SerializedName("severity")
    LogSeverity severity;

    @SerializedName("msg")
    String message;

    public Log(LocalDateTime timestamp, LogSeverity severity, String message) {
        this.timestamp = timestamp;
        this.severity = severity;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LogSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(LogSeverity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
