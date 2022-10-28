package model;

// Статус для задачи, подзадачи и эпика
public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В прогрессе"),
    DONE("Выполнен");
    private String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
