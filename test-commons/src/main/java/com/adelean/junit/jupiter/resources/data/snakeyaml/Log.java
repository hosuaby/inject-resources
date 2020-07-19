package com.adelean.junit.jupiter.resources.data.snakeyaml;

import com.adelean.junit.jupiter.resources.data.LogSeverity;

import java.util.Date;
import java.util.Objects;

public class Log {
    public Date date;
    public LogSeverity severity;
    public String msg;

    public Log() {
    }

    public Log(Date date, LogSeverity severity, String msg) {
        this.date = date;
        this.severity = severity;
        this.msg = msg;
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
        return Objects.equals(date, another.date)
                && severity == another.severity
                && Objects.equals(msg, another.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, severity, msg);
    }
}
