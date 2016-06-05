package ru.thecop.tinkofftest.stats;

import lombok.Value;
import ru.thecop.tinkofftest.entry.Entry;
import ru.thecop.tinkofftest.entry.EntryType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsManager {

    private final Map<EntryMapKey, LocalDateTime> currentEntriesMap = new HashMap<>();

    private final Map<StatsMapKey, MethodStats> statsMap = new HashMap<>();

    //TODO rename
    public void addEntry(Entry entry) {
        EntryMapKey entryKey = new EntryMapKey(entry);
        //in case log has exit-lines without entry-lines
        if (entry.getType() == EntryType.ENTRY) {
            currentEntriesMap.put(entryKey, entry.getDateTime());
            return;
        }
        LocalDateTime startDateTime = currentEntriesMap.remove(entryKey);
        if (startDateTime == null) {
            //No start entry - ignore
            return;
        }
        Duration duration = Duration.between(startDateTime, entry.getDateTime());
        StatsMapKey statsMapKey = new StatsMapKey(entry);
        MethodStats methodStats = statsMap.get(statsMapKey);
        if (methodStats == null) {
            methodStats = new MethodStats(entry.getLoggedClass(), entry.getMethodName());
            statsMap.put(statsMapKey, methodStats);
        }
        methodStats.addCall(duration.toMillis(), entry.getCallId());
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

        EntryMapKey(Entry entry) {
            loggedClass = entry.getLoggedClass();
            methodName = entry.getMethodName();
            callId = entry.getCallId();
        }
    }
}
