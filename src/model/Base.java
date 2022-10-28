package model;

import java.util.ArrayList;

public abstract class Base {
    // Идентификатор
    public int id;
    // Заголовок
    public String title;

    protected ArrayList<String> descriptions;

    public Status status;

    public Base(int id, String title) {
        this.id = id;
        this.title = title;
        this.descriptions = null;
    }

    public long getId() {
        return id;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        if (this.descriptions == null) {
            this.descriptions = new ArrayList<>();
        }
        this.descriptions = descriptions;
    }

    public abstract void setStatus(Status status);
}
