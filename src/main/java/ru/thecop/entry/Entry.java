package ru.thecop.entry;

import java.time.LocalDateTime;

public final class Entry {
    //TODO lombok?

    private EntryType type;
    private LocalDateTime dateTime;
    private String loggedClass;
    private String methodName;
    private long callId;

    public Entry(EntryType type, LocalDateTime dateTime, String loggedClass, String methodName, long callId) {
        this.type = type;
        this.dateTime = dateTime;
        this.loggedClass = loggedClass;
        this.methodName = methodName;
        this.callId = callId;
    }

    public EntryType getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLoggedClass() {
        return loggedClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getCallId() {
        return callId;
    }


    @Override
    public String toString() {
        return "Entry{" +
                "type=" + type +
                ", dateTime=" + dateTime +
                ", loggedClass='" + loggedClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", callId=" + callId +
                '}';
    }
}
