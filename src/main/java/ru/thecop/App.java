package ru.thecop;

import ru.thecop.entry.Entry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        logMem();
        System.out.println("Hello World!");
//        readStream();
//        readInMem();
        parseLine("2015-10-26T16:09:56,885 TRACE [OperationsImpl] entry with (processClient:17899)");

    }

    private static void readStream() {
        Path path = Paths.get("testlog.log");
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> inc());
            logMem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println(i);
    }

    private static void readInMem() {
        Path path = Paths.get("testlog.log");
        try {
            List<String> list = Files.readAllLines(path);
            System.err.println("readInMem = " + list.size());
            logMem();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Entry parseLine(String line){
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            System.err.println(matcher.group(1));
        }
        return null;
    }

    private static long i = 0;

    private static void inc() {
        i++;
    }

    private static void logMem() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
        sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
        sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
        sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
        System.err.println(sb);
    }
}
