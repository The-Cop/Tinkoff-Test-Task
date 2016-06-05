package ru.thecop.tinkofftest.entry;

public enum EntryType {

    ENTRY, EXIT;

    public EntryType reverse() {
        return this == ENTRY ? EXIT : ENTRY;
    }
}
