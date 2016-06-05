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

    public void process(Path filePath) {
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.parallel().forEach(this::processLine);
        } catch (IOException e) {
            log.error("Failed to open file {}", e.getMessage());
        }
    }

    private void processLine(String line) {
        Entry entry = LineParser.parseLine(line);
        if (entry == null) {
            //malformed line, ignore
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
        Path filePath = Paths.get(args[0]);
        if (!Files.exists(filePath)) {
            System.err.println("Can not find file " + filePath);
            return;
        }
        LogProcessor p = new LogProcessor();
        p.process(filePath);
        p.getStatsManager().printStatsToConsole();
    }
}
