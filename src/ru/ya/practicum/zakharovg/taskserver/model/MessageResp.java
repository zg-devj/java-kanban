package ru.ya.practicum.zakharovg.taskserver.model;

// Для сообщения сервера в JSON формате
public class MessageResp {
    private String message;

    public MessageResp() {
    }

    public MessageResp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
