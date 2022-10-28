package model;

import java.util.ArrayList;

public abstract class Base {
    // Идентификатор
    public int id;
    // Заголовок
    public String title;
    // Описание
    protected ArrayList<String> descriptions;
    // Статус задачи
    public Status status;

    public Base(int id, String title) {
        this.id = id;
        this.title = title;
        this.descriptions = null;
    }

    public Base(String title) {
        this.title = title;
        this.descriptions = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    protected void setStatus(Status status){
        this.status = status;
    }

    protected Status getStatus(){
        return status;
    }
}
