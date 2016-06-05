package ru.thecop.tinkofftest.stats;

import lombok.Value;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class MethodStats {

    private final String loggedClass;
    private final String methodName;
    private final AtomicLong minTime;
    private final AtomicReference<MaximumInfo> maximumInfoRef;
    private final AtomicLong callCount;
    private final AtomicLong totalTimeMillis;

    public MethodStats(String loggedClass, String methodName) {
        this.loggedClass = loggedClass;
        this.methodName = methodName;
        minTime = new AtomicLong(Long.MAX_VALUE);
        maximumInfoRef = new AtomicReference<>(new MaximumInfo(Long.MIN_VALUE, -1));
        callCount = new AtomicLong();
        totalTimeMillis = new AtomicLong();
    }

    public MethodStats(MethodStats other) {
        loggedClass = other.loggedClass;
        methodName = other.methodName;
        minTime = other.minTime;
        maximumInfoRef = new AtomicReference<>(other.maximumInfoRef.get());
        callCount = new AtomicLong(other.callCount.get());
        totalTimeMillis = other.totalTimeMillis;
    }

    public void addCall(long durationMillis, long callId) {
        callCount.incrementAndGet();
        totalTimeMillis.addAndGet(durationMillis);
        checkAndSetMaxTime(durationMillis, callId);
        checkAndSetMinTime(durationMillis);
    }

    private void checkAndSetMaxTime(long durationMillis, long callId) {
        MaximumInfo newMaximumInfo = new MaximumInfo(durationMillis, callId);
        while (true) {
            MaximumInfo currentMaximum = maximumInfoRef.get();
            if (durationMillis <= currentMaximum.getMaxTime()) {
                //new duration is not a new maximum
                return;
            }
            if (maximumInfoRef.compareAndSet(currentMaximum, newMaximumInfo)) {
                //successful set new maximum
                return;
            }
            // keep trying if maximum changed by another thread
        }
    }

    private void checkAndSetMinTime(long durationMillis) {
        while (true) {
            long currentMinimum = minTime.get();
            if (durationMillis >= currentMinimum) {
                //new duration is not a new minimum
                return;
            }
            if (minTime.compareAndSet(currentMinimum, durationMillis)) {
                //successful set new minimum
                return;
            }
            // keep trying if minTime changed by another thread
        }
    }

    private long avg() {
        if (callCount.get() == 0) {
            return 0;
        }
        return totalTimeMillis.get() / callCount.get();
    }

    public String print() {
        return String.format("%s:%s min %d, max %d, avg %d, max id %d, count %d",
                loggedClass, methodName, minTime.get(), maximumInfoRef.get().getMaxTime(), avg(),
                maximumInfoRef.get().getMaxTimeCallId(), callCount.get(), totalTimeMillis.get());
    }

    @Value
    private static final class MaximumInfo {

        private final long maxTime;
        private final long maxTimeCallId;
    }
}
