package model;

public abstract class Base {
    // Идентификатор
    protected int id;
    // Заголовок
    protected String title;
    // Описание
    protected String descriptions;
    // Статус задачи
    protected Status status;

    public Base(String title, String descriptions) {
        this.title = title;
        this.descriptions = descriptions;
    }

    public Base(String title) {
        this(title, null);
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
}