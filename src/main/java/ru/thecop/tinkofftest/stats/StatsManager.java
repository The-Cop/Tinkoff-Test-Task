package ru.thecop.tinkofftest.stats;

import lombok.Value;
import ru.thecop.tinkofftest.entry.Entry;
import ru.thecop.tinkofftest.entry.EntryType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StatsManager {

    private final Map<EntryMapKey, LocalDateTime> currentEntriesMap = new ConcurrentHashMap<>();
    private final Map<StatsMapKey, MethodStats> statsMap = new ConcurrentHashMap<>();

    //TODO rename
    public void addToStats(Entry entry) {
        EntryMapKey pairKey = EntryMapKey.ofPair(entry);
        LocalDateTime pairDateTime = currentEntriesMap.remove(pairKey);
        if (pairDateTime == null) {
            //no pair entry - add current
            currentEntriesMap.put(EntryMapKey.of(entry), entry.getDateTime());
            return;
        }
        Duration duration = Duration.between(pairDateTime, entry.getDateTime());
        StatsMapKey statsMapKey = new StatsMapKey(entry);
        MethodStats methodStats = statsMap.get(statsMapKey);
        if (methodStats == null) {
            methodStats = new MethodStats(entry.getLoggedClass(), entry.getMethodName());
            statsMap.putIfAbsent(statsMapKey, methodStats);
        }
        methodStats.addCall(Math.abs(duration.toMillis()), entry.getCallId());
    }

    public void printStatsToConsole() {
        statsMap.values().stream().forEach(ms -> System.out.println(ms.print()));
    }

    /**
     * @return list of copies of methods stats
     */
    public List<MethodStats> getMethodStats() {
        return statsMap.values().stream().map(MethodStats::new).collect(Collectors.toList());
    }

    @Value
    private static final class StatsMapKey {

        private final String loggedClass;
        private final String methodName;

        StatsMapKey(Entry entry) {
            loggedClass = entry.getLoggedClass();
            methodName = entry.getMethodName();
        }
    }

    @Value
    private static final class EntryMapKey {

        private final String loggedClass;
        private final String methodName;
        private final long callId;
        private final EntryType type;

        private EntryMapKey(Entry entry, boolean pair) {
            loggedClass = entry.getLoggedClass();
            methodName = entry.getMethodName();
            callId = entry.getCallId();
            type = pair ? entry.getType().reverse() : entry.getType();
        }

        /**
         * @return An exit-typed-key if entry is of type "entry" and vice versa.
         */
        public static EntryMapKey ofPair(Entry entry) {
            return new EntryMapKey(entry, true);
        }

        public static EntryMapKey of(Entry entry) {
            return new EntryMapKey(entry, false);
        }
    }
}
