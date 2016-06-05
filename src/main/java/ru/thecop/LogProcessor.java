package ru.thecop;

import ru.thecop.entry.Entry;
import ru.thecop.stats.StatsManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LogProcessor {
    private StatsManager statsManager;

    public LogProcessor() {
        statsManager = new StatsManager();
    }

    public void process(String filePath) {
        Path path = Paths.get(filePath);
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(this::processLine);
        } catch (IOException e) {
            System.err.println("Can not open file: " + e.getMessage());
            return;
        }
        statsManager.printStatsToConsole();
    }

    private void processLine(String line) {
        Entry entry = LineParser.parseLine(line);
        if (entry == null) {
            //malformed line
            return;
        }
        statsManager.addEntry(entry);
    }

    //TODO checkstyle
    public static void main(String[] args) {
        if(args.length==0){
            System.err.println("No file path specified.");
            LogProcessor p = new LogProcessor();
            p.process("testlog.log");
            return;
        }
        LogProcessor p = new LogProcessor();
        p.process(args[0]);
    }
}
