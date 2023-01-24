package ru.ya.practicum.zakharovg.taskserver.model;

// - Класс относится и используется только HttpTaskServer
//   и не имеет никакого отношения к java-kanban
// - служит только для возврата сообщения отличного от "нормального"
// - вид {"message":"сообщение"} мне кажется более информативным чем "сообщение"
// - источник как пример : https://www.baeldung.com/rest-api-error-handling-best-practices
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
