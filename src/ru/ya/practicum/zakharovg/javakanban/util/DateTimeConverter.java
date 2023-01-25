package ru.ya.practicum.zakharovg.javakanban.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
    private final static String OUTPUT_DATETIME_FORMAT = "dd.MM.yyyy HH:mm";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(OUTPUT_DATETIME_FORMAT);
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static Instant fromStringToInstant(String datetime) {
        LocalDateTime dateTime = LocalDateTime.parse(datetime, DATE_TIME_FORMATTER);
        return ZonedDateTime.of(dateTime, ZONE_ID).toInstant();
    }

    public static Instant fromMilliToInstant(Long milli) {
        return Instant.ofEpochMilli(milli);
    }

    public static String fromInstantToString(Instant instant) {
        if (instant != null) {
            ZonedDateTime zonedDateTime1 = ZonedDateTime.ofInstant(instant, ZONE_ID);
            return zonedDateTime1.format(DateTimeFormatter.ofPattern(OUTPUT_DATETIME_FORMAT));
        }
        return null;
    }
}
