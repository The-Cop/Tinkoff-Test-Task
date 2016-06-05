package ru.thecop.tinkofftest;

import org.junit.Test;
import ru.thecop.tinkofftest.stats.MethodStats;
import ru.thecop.tinkofftest.stats.StatsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

public class LogProcessorTest {

    @Test
    public void testLogProcessor() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("testlog.log").getFile());

        LogProcessor logProcessor = new LogProcessor();
        logProcessor.process(file.getPath());
        StatsManager statsManager = logProcessor.getStatsManager();
        List<MethodStats> actualStats = statsManager.getMethodStats();
        List<MethodStats> expectedStats = createExpectedMethodStats();

        assertTrue(actualStats.containsAll(expectedStats));
        assertTrue(expectedStats.containsAll(actualStats));

        List<String> actualPrinted = actualStats.stream().map(MethodStats::print).collect(Collectors.toList());
        List<String> expectedPrinted = createExpectedPrintedStats();

        assertTrue(actualPrinted.containsAll(expectedPrinted));
        assertTrue(expectedPrinted.containsAll(actualPrinted));
    }

    private static List<String> createExpectedPrintedStats() {
        List<String> expectedPrinted = new ArrayList<>();
        expectedPrinted.add(
                "OperationsImpl:processClient min 10000, max 10000, avg 10000, max id 17893, count 1, totaltime 10000");
        expectedPrinted.add(
                "OperationsImpl:getData min 20000, max 20000, avg 20000, max id 17894, count 1, totaltime 20000");
        expectedPrinted.add(
                "OperationsImpl:getActions min 30000, max 120000, avg 75000, max id 17896, count 2, totaltime 150000");
        return expectedPrinted;
    }

    private static List<MethodStats> createExpectedMethodStats() {
        List<MethodStats> expectedStats = new ArrayList<>();
        MethodStats methodStats1 = new MethodStats("OperationsImpl", "processClient");
        methodStats1.addCall(10000, 17893);
        MethodStats methodStats2 = new MethodStats("OperationsImpl", "getData");
        methodStats2.addCall(20000, 17894);
        MethodStats methodStats3 = new MethodStats("OperationsImpl", "getActions");
        methodStats3.addCall(30000, 17895);
        methodStats3.addCall(120000, 17896);
        expectedStats.add(methodStats1);
        expectedStats.add(methodStats2);
        expectedStats.add(methodStats3);
        return expectedStats;
    }
}
