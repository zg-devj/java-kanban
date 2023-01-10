package model;

import util.DateTimeConverter;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
    protected Duration duration = null;

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

    // TODO: 09.01.2023 test
    public Instant getStartTime() {
        return startTime;
    }

    // TODO: 09.01.2023 test
    public void setStartTime(long milli) {
        this.startTime = DateTimeConverter.fromMilliToInstant(milli);
    }

    public void setStartTime(Instant instant) {
        this.startTime = instant;
    }

    // TODO: 09.01.2023 test

    /**
     * Установить строкой время старта задачи
     *
     * @param dateTime format "dd.MM.yyyy HH:mm"
     */
    public void setStartTime(String dateTime) {
        this.startTime = DateTimeConverter.fromStringToInstant(dateTime);
    }

    // TODO: 09.01.2023 test
    public long getDurationMinute() {
        return duration.toMinutes();
    }

    // TODO: 09.01.2023 test
    public void setDurationMinute(long minute) {
        this.duration = Duration.ofMinutes(minute);
    }

    // TODO: 09.01.2023 test
    public Instant getEndTime() {
        if(startTime!=null) {
            return startTime.plusSeconds(duration.toSeconds());
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTask task = (BaseTask) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(descriptions, task.descriptions) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, descriptions, status);
    }
}