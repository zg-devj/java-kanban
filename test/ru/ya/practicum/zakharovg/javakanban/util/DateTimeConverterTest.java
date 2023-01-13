package ru.ya.practicum.zakharovg.javakanban.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeConverterTest {
    @Test
    public void fromStringToInstant() {
        String datetime = "10.01.2023 12:30";

        Instant actual = Instant.ofEpochMilli(1673343000000L);
        Instant expected = DateTimeConverter.fromStringToInstant(datetime);

        assertEquals(expected, actual, "Даты не совпадают");
    }

    @Test
    public void fromInstantToString() {
        Instant datetime = Instant.ofEpochMilli(1673343000000L);

        String actual = DateTimeConverter.fromInstantToString(datetime);
        String expected = "10.01.2023 12:30";

        assertEquals(expected, actual, "Даты не совпадают");
    }
}