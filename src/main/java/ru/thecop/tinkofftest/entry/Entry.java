package ru.thecop.tinkofftest.entry;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public final class Entry {

    private final EntryType type;
    private final LocalDateTime dateTime;
    private final String loggedClass;
    private final String methodName;
    private final long callId;
}
