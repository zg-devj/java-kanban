package ru.ya.practicum.zakharovg.javakanban.model;

import ru.ya.practicum.zakharovg.javakanban.util.DateTimeConverter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public abstract class BaseTask {
    // Идентификатор
    protected Integer id;
    // Заголовок
    protected String title;
    // Описание
    protected String descriptions;
    // Статус задачи
    protected Status status;
    // момент начала задачи
    protected Instant startTime = null;
    // период
    // 0 - это значение по умолчанию,
    // зачем использовать null, если 0 - это отсутствие времени?
    protected Duration duration = Duration.ZERO;

    public BaseTask(String title, String descriptions) {
        this.title = title;
        this.descriptions = descriptions;
    }

    public BaseTask(String title, String descriptions, Instant startTime, long minuteDuration) {
        this(title, descriptions);
        setDurationMinute(minuteDuration);
        this.startTime = startTime;
    }

    public BaseTask(String title, String descriptions, String startTime, long minuteDuration) {
        this(title, descriptions);
        setDurationMinute(minuteDuration);
        if (startTime != null) {
            setStartTime(startTime);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant instant) {
        this.startTime = instant;
    }

    /**
     * Установить строкой время старта задачи
     *
     * @param dateTime format "dd.MM.yyyy HH:mm"
     */
    public void setStartTime(String dateTime) {
        this.startTime = DateTimeConverter.fromStringToInstant(dateTime);
    }

    public long getDurationMinute() {
        return duration.toMinutes();
    }

    public void setDurationMinute(long minute) {
        this.duration = Duration.ofMinutes(minute);
    }

    public Instant getEndTime() {
        if (startTime != null) {
            return startTime.plusSeconds(duration.toSeconds());
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTask task = (BaseTask) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
