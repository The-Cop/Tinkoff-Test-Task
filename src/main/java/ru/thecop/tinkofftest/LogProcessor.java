package ru.thecop.tinkofftest;

import lombok.extern.slf4j.Slf4j;
import ru.thecop.tinkofftest.entry.Entry;
import ru.thecop.tinkofftest.stats.StatsManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class LogProcessor {

    private final StatsManager statsManager;

    public LogProcessor() {
        statsManager = new StatsManager();
    }

    //TODO remove this method
    public void process(String filePath) {
        Path path = Paths.get(filePath);
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(this::processLine);
        } catch (IOException e) {
            log.error("Failed to open file {}", e.getMessage());
        }
    }

    //TODO rename to process()
    public void processPar(String filePath) {
        Path path = Paths.get(filePath);
        try (Stream<String> stream = Files.lines(path)) {
            stream.parallel().forEach(this::processLine);
        } catch (IOException e) {
            log.error("Failed to open file {}", e.getMessage());
        }
    }

    private void processLine(String line) {
        Entry entry = LineParser.parseLine(line);
        if (entry == null) {
            //malformed line
            return;
        }
        statsManager.addToStats(entry);
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No file path specified.");
            return;
        }
        //TODO remove timings
        long start = System.currentTimeMillis();
        LogProcessor p = new LogProcessor();
        p.processPar(args[0]);
        p.getStatsManager().printStatsToConsole();
        long end = System.currentTimeMillis();
        System.err.println("Parallel " + (double) (end - start) / 1000);

        //TODO remove this
        start = System.currentTimeMillis();
        p = new LogProcessor();
        p.process(args[0]);
        p.getStatsManager().printStatsToConsole();
        end = System.currentTimeMillis();
        System.err.println("Single " + (double) (end - start) / 1000);
    }
}
