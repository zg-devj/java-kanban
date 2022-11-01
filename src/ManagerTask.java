import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerTask {
    // Идентификаторы для Task

    public HashMap<Integer, Task> tasks;
    Identifier taskIdentifier;
    public HashMap<Integer, Subtask> subtasks;
    Identifier subtaskIdentifier;
    public HashMap<Integer, Epic> epics;
    Identifier epicIdentifier;

    public ManagerTask() {
        this.tasks = new HashMap<>();
        this.taskIdentifier = new Identifier();
        this.subtasks = new HashMap<>();
        this.subtaskIdentifier = new Identifier();
        this.epics = new HashMap<>();
        this.epicIdentifier = new Identifier();
    }

    //region Task методы
    // Получение Task по идентификатору.
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Получение всех записей Task-ов
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Cоздание Task
    public void addTask(Task task) {
        task.setId(taskIdentifier.next());
        tasks.putIfAbsent(task.getId(), task);
    }

    // Обновление Task
    public void updateTask(Task task) {
        // task.getId() не может вернуть null, возвращает int
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    // Удаление Task
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            // Удаляем Task
            tasks.remove(id);
        }
    }

    // Удаление все Tasks
    public void deleteAllTasks() {
        tasks.clear();
    }
    //endregion

    //region Subtask методы
    // Получение Subtask по идентификатору.
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // Получение всех записей Subtask-ов
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Возвращаем вписок Subtasks по id эпика
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return subtasks.values()
                .stream()
                .filter(s -> s.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    // Обновление Subtask
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            // Обновляем статус
            updateEpicStatus(subtask.getEpicId());
        }
    }

    // Удаление Subtask
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = getSubtaskById(id).getEpicId();
            // Отвязываем Subtask от Epic (удалить id сабтасков из списка в эпике)
            getEpicById(epicId).getSubtaskIds().remove(id);
            // Удаляем Subtask
            subtasks.remove(id);
            // Обновляем статус у Epic
            updateEpicStatus(epicId);
        }
    }

    // Удалить все Subtask
    public void deleteAllSubtasks() {
        for (Subtask subtask : getAllSubtasks()) {
            // удалить id сабтасков из списка в эпике
            deleteSubtask(subtask.getId());
            // Обновляем статус
            updateEpicStatus(subtask.getEpicId());
        }
    }
    //endregion

    //region Epic методы
    // Получение Epic по идентификатору.
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    // Получение всех записей Epic-ов
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Cоздание Epic
    public void addEpic(Epic epic) {
        epic.setId(epicIdentifier.next());
        epics.putIfAbsent(epic.getId(), epic);
    }

    // Создаем Subtask и добавляем его к эпику
    // Ваше замечание ... не должно быть возможности создать сабтаск без эпика
    // addSubtask был приватным и использовался только в этом методе (удалил метод)
    // Во время добавления сабтаска к эпику, сабтаск не имеет id эпика
    public void addSubtaskToEpic(int epicId, String subtaskTitle, String subtaskDescription) {
        if (epics.containsKey(epicId)) {
            // Создаем Subtask
            Subtask subtask = new Subtask(subtaskTitle, epicId, subtaskDescription);
            subtask.setId(subtaskIdentifier.next());
            subtasks.put(subtask.getId(), subtask);
            // Привязываем к epic
            epics.get(epicId).add(subtask);
            // Обновляем статус
            updateEpicStatus(epicId);
        }
    }

    // Обновление Epic
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // Удаление Epic
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            // Удаляем Subtask для Epic
            // getSubtaskIds() возвращает HeshSet что при удалении
            // в методе deleteSubtask вызывает ошибку
            // Статья о проблеме https://www.techiedelight.com/remove-elements-from-set-java/
            Integer[] list = epics.get(id).getSubtaskIds().toArray(new Integer[0]);
            for (Integer delId : list) {
                // Удаляем сабтаски
                deleteSubtask(delId);
            }
            // Удаляем Epic
            epics.remove(id);
        }
    }

    // Удалить все Epic
    public void deleteAllEpics() {
        for (Epic epic : getAllEpics()) {
            // метод deleteEpic внутри удаляет epic и его сабтаски
            deleteEpic(epic.getId());
        }
    }
    //endregion

    // Обновление статуса Epic
    private void updateEpicStatus(int epicId) {
        boolean isNew = false;
        boolean isInProgress = false;
        boolean isDone = false;
        if (getSubtasksByEpicId(epicId).size() == 0) {
            epics.get(epicId).setStatus(Status.NEW);
            return;
        }
        for (Subtask subtask : getSubtasksByEpicId(epicId)) {
            switch (subtask.getStatus()) {
                case NEW:
                    isNew = true;
                    break;
                case IN_PROGRESS:
                    isInProgress = true;
                    break;
                case DONE:
                    isDone = true;
                    break;
            }
        }
        if (isDone && !isNew && !isInProgress) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if (isInProgress || (isNew && isDone)) {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        } else {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }
}
