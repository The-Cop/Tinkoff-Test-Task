package ru.thecop.tinkofftest;

import org.junit.Test;
import ru.thecop.tinkofftest.stats.MethodStats;

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
        logProcessor.processPar(file.getPath());

        List<String> actualPrinted = logProcessor.getStatsManager().getMethodStats()
                .stream()
                .map(MethodStats::print)
                .collect(Collectors.toList());
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
}
