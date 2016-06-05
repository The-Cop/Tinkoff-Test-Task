package ru.thecop.tinkofftest.stats;

public final class MethodStats {
    private String loggedClass;
    private String methodName;
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
        this.loggedClass = other.loggedClass;
        this.methodName = other.methodName;
        this.minTime = other.minTime;
        this.maxTime = other.maxTime;
        this.maxTimeCallId = other.maxTimeCallId;
        this.callCount = other.callCount;
        this.totalTimeMillis = other.totalTimeMillis;
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
                loggedClass, methodName, minTime, maxTime, avg(), maxTimeCallId, callCount,totalTimeMillis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodStats that = (MethodStats) o;

        if (minTime != that.minTime) return false;
        if (maxTime != that.maxTime) return false;
        if (maxTimeCallId != that.maxTimeCallId) return false;
        if (callCount != that.callCount) return false;
        if (totalTimeMillis != that.totalTimeMillis) return false;
        if (loggedClass != null ? !loggedClass.equals(that.loggedClass) : that.loggedClass != null) return false;
        return methodName != null ? methodName.equals(that.methodName) : that.methodName == null;

    }

    @Override
    public int hashCode() {
        int result = loggedClass != null ? loggedClass.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (int) (minTime ^ (minTime >>> 32));
        result = 31 * result + (int) (maxTime ^ (maxTime >>> 32));
        result = 31 * result + (int) (maxTimeCallId ^ (maxTimeCallId >>> 32));
        result = 31 * result + (int) (callCount ^ (callCount >>> 32));
        result = 31 * result + (int) (totalTimeMillis ^ (totalTimeMillis >>> 32));
        return result;
    }
}
