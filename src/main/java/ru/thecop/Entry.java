package ru.thecop;

import java.time.LocalDateTime;

/**
 * Created by TheCops-PC on 01.06.2016.
 */
public final class Entry {
    //TODO lombok?

    private LocalDateTime dateTime;
    private String loggedClass;
    private String methodName;
    private long callId;

    public Entry(LocalDateTime dateTime, String loggedClass, String methodName, long callId) {
        this.dateTime = dateTime;
        this.loggedClass = loggedClass;
        this.methodName = methodName;
        this.callId = callId;
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
                "dateTime=" + dateTime +
                ", loggedClass='" + loggedClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", callId=" + callId +
                '}';
    }
}
