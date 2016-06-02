package ru.thecop.stats;

import ru.thecop.entry.Entry;
import ru.thecop.entry.EntryType;

import java.time.Duration;
import java.util.HashMap;

public class StatsManager {
    private HashMap<EntryMapKey, Entry> currentEntriesMap = new HashMap<>();

    private HashMap<StatsMapKey, MethodStats> statsMap = new HashMap<>();

    //TODO rename
    public void addEntry(Entry entry) {
        EntryMapKey entryKey = new EntryMapKey(entry);
        if (entry.getType() == EntryType.ENTRY) {
            currentEntriesMap.put(entryKey, entry);
            return;
        }
        Entry startEntry = currentEntriesMap.remove(entryKey);
        if (startEntry == null) {
            //No start entry - ignore
            return;
        }
        Duration duration = Duration.between(startEntry.getDateTime(), entry.getDateTime());
        StatsMapKey statsMapKey = new StatsMapKey(entry.getLoggedClass(), entry.getMethodName());
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


    //Lombok
    private static final class StatsMapKey {
        private String loggedClass;
        private String methodName;

        //TODO entry, not two strings
        public StatsMapKey(String loggedClass, String methodName) {
            this.loggedClass = loggedClass;
            this.methodName = methodName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StatsMapKey that = (StatsMapKey) o;

            if (!loggedClass.equals(that.loggedClass)) return false;
            return methodName.equals(that.methodName);

        }

        @Override
        public int hashCode() {
            int result = loggedClass.hashCode();
            result = 31 * result + methodName.hashCode();
            return result;
        }
    }

    private static final class EntryMapKey {
        private String loggedClass;
        private String methodName;
        private long callId;

        public EntryMapKey(Entry entry) {
            loggedClass = entry.getLoggedClass();
            methodName = entry.getMethodName();
            callId = entry.getCallId();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EntryMapKey entryMapKey = (EntryMapKey) o;

            if (callId != entryMapKey.callId) return false;
            if (loggedClass != null ? !loggedClass.equals(entryMapKey.loggedClass) : entryMapKey.loggedClass != null)
                return false;
            return methodName != null ? methodName.equals(entryMapKey.methodName) : entryMapKey.methodName == null;

        }

        @Override
        public int hashCode() {
            int result = loggedClass != null ? loggedClass.hashCode() : 0;
            result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
            result = 31 * result + (int) (callId ^ (callId >>> 32));
            return result;
        }
    }
}
