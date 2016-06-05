package ru.thecop.tinkofftest;

import ru.thecop.tinkofftest.entry.Entry;
import ru.thecop.tinkofftest.entry.EntryType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LineParser {

    private LineParser() {
    }

    private static final Pattern PATTERN = Pattern.compile("([\\S]+).*\\[([^)]+)\\] ([\\S]+).*\\(([^:]+):([^\\)]+)");
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss','SSS");

    public static Entry parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            try {
                String dateTimeString = matcher.group(1);
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);

                String loggedClass = matcher.group(2);
                String type = matcher.group(3);
                String methodName = matcher.group(4);
                String callId = matcher.group(5);

                return new Entry(EntryType.valueOf(type.toUpperCase()), dateTime,
                        loggedClass, methodName, Long.valueOf(callId));
            } catch (IllegalArgumentException | DateTimeParseException e) {
                //TODO log exceptions
                //failed to parse line: not an entry and not an exit, or callId not a number, or wrong date
                //skip line
            }
        }
        return null;
    }
}
