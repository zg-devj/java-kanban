package model;

import java.util.ArrayList;

public abstract class Base {
    // Идентификатор
    protected int id;
    // Id родителя
    private int parentId;
    // Заголовок
    protected String title;
    // Описание
    protected String descriptions;
    // Статус задачи
    protected Status status;

    public Base(String title) {
        this.title = title;
        this.descriptions = null;
        this.parentId=0;
    }

    public String getDescriptions() {
        return descriptions;
    }

    // Устанавливаем описание
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}