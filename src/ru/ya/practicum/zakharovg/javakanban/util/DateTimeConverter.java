package ru.ya.practicum.zakharovg.javakanban.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
    private final static DateTimeFormatter FORMATTER_INPUT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final static String OUTPUT_FORMAT = "dd.MM.yy HH:mm";
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static Instant fromStringToInstant(String datetime) {
        LocalDateTime dateTime = LocalDateTime.parse(datetime, FORMATTER_INPUT);
        return ZonedDateTime.of(dateTime, ZONE_ID).toInstant();
    }

    public static Instant fromMilliToInstant(Long milli) {
        return Instant.ofEpochMilli(milli);
    }

    public static String fromInstantToString(Instant instant) {
        if(instant!=null) {
            ZonedDateTime zonedDateTime1 = ZonedDateTime.ofInstant(instant, ZONE_ID);
            return zonedDateTime1.format(DateTimeFormatter.ofPattern(OUTPUT_FORMAT));
        }
        return "Не указано";
    }
}