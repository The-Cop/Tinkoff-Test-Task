package ru.thecop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LineParser {
    private LineParser() {
    }

    private static final Pattern PATTERN = Pattern.compile("([\\S]+).*\\[([^)]+)\\].*\\(([^:]+):([^\\)]+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss','SSS");

    public static Entry parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            String timestamp = matcher.group(1);
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DATE_TIME_FORMATTER);
            String loggedClass = matcher.group(2);
            String methodName = matcher.group(3);
            String duration = matcher.group(4);
            return new Entry(dateTime, loggedClass, methodName, Long.valueOf(duration));
        }
        return null;
    }

    public static void main(String[] args) {
        String line = "2015-10-26T16:10:11,403 TRACE [OperationsImpl] entry with (processAction:17914)";
        System.err.println(line);
        Entry e = parseLine(line);
        System.err.println(e);
    }
}
