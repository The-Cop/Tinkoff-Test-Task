package ru.thecop.tinkofftest;

import org.junit.Test;
import ru.thecop.tinkofftest.entry.Entry;
import ru.thecop.tinkofftest.entry.EntryType;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static ru.thecop.tinkofftest.LineParser.DATE_TIME_FORMATTER;

public class LineParserTest {

    @Test
    public void testNormal() {
        Entry e = LineParser.parseLine(
                "2015-10-26T16:10:01,733 TRACE [OperationsImpl] entry with (processAction:17904)");
        assertNotNull(e);
        assertEquals(LocalDateTime.parse("2015-10-26T16:10:01,733", DATE_TIME_FORMATTER), e.getDateTime());
        assertEquals("OperationsImpl", e.getLoggedClass());
        assertEquals(EntryType.ENTRY, e.getType());
        assertEquals("processAction", e.getMethodName());
        assertEquals(17904, e.getCallId());
    }

    @Test
    public void testWrongType() {
        Entry e = LineParser.parseLine(
                "2015-10-26T16:10:01,733 TRACE [OperationsImpl] zzentry with (processAction:17904)");
        assertNull(e);
    }

    @Test
    public void testWrongCallId() {
        Entry e = LineParser.parseLine(
                "2015-10-26T16:10:01,733 TRACE [OperationsImpl] entry with (processAction:17a904)");
        assertNull(e);
    }

    @Test
    public void testWrongDate() {
        Entry e = LineParser.parseLine(
                "2015-10-26Z16:10:01,733 TRACE [OperationsImpl] entry with (processAction:17904)");
        assertNull(e);
    }
}
