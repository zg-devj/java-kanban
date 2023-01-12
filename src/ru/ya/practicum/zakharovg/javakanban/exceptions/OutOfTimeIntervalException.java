package ru.ya.practicum.zakharovg.javakanban.exceptions;

public class OutOfTimeIntervalException extends RuntimeException {
    public OutOfTimeIntervalException(String message) {
        super(message);
    }
}
