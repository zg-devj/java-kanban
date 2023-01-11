package model;

import util.DateTimeConverter;
import util.SortedBaseTask;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BaseTask {
    final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    // Идентификатор
    protected int id;
    // Заголовок
    protected String title;
    // Описание
    protected String descriptions;
    // Статус задачи
    protected Status status;
    // момент начала задачи
    protected Instant startTime = null;
    // период
    protected Duration duration = Duration.ZERO;

    public BaseTask(String title, String descriptions) {
        this.title = title;
        this.descriptions = descriptions;
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

