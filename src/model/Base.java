package model;

import java.util.ArrayList;

public abstract class Base {
    // Идентификатор
    protected int id;
    // Заголовок
    public String title;
    // Описание
    protected ArrayList<String> descriptions;
    // Статус задачи
    protected Status status;

    public Base(String title) {
        this.title = title;
        this.descriptions = null;
    }

    // Устанавливаем описание
    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}