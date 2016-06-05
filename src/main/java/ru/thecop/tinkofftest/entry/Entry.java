package ru.thecop.tinkofftest.entry;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public final class Entry {

    private EntryType type;
    private LocalDateTime dateTime;
    private String loggedClass;
    private String methodName;
    private long callId;
}
