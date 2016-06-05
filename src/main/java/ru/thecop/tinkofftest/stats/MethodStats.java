package ru.thecop.tinkofftest.stats;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class MethodStats {

    private final String loggedClass;
    private final String methodName;
    private long minTime;
    private long maxTime;
    private long maxTimeCallId;
    private long callCount;
    private long totalTimeMillis;

    public MethodStats(String loggedClass, String methodName) {
        this.loggedClass = loggedClass;
        this.methodName = methodName;
        minTime = Long.MAX_VALUE;
        maxTime = Long.MIN_VALUE;
    }

    public MethodStats(MethodStats other) {
       loggedClass = other.loggedClass;
       methodName = other.methodName;
       minTime = other.minTime;
       maxTime = other.maxTime;
       maxTimeCallId = other.maxTimeCallId;
       callCount = other.callCount;
       totalTimeMillis = other.totalTimeMillis;
    }

    public void addCall(long durationMillis, long callId) {
        callCount++;
        totalTimeMillis += durationMillis;
        if (maxTime < durationMillis) {
            maxTime = durationMillis;
            maxTimeCallId = callId;
        }
        if (minTime > durationMillis) {
            minTime = durationMillis;
        }
    }

    private long avg() {
        return totalTimeMillis / callCount;
    }

    public String print() {
        return String.format("%s:%s min %d, max %d, avg %d, max id %d, count %d, totaltime %d",
                loggedClass, methodName, minTime, maxTime, avg(), maxTimeCallId, callCount, totalTimeMillis);
    }
}
