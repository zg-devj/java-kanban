package service.impl;

import model.*;
import service.TaskManager;
import util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    // Идентификаторы для Task, Subtask, Epic
    private Identifier idGen;

    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Subtask> subtasks;
    public HashMap<Integer, Epic> epics;

    public List<BaseTask> history;

    public InMemoryTaskManager() {
        this.idGen = new Identifier();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.history = new ArrayList<>();
    }

    //region Task методы
    // Получение Task по идентификатору.
    public Task getTask(int id) {
        return tasks.get(id);
    }

    // Получение всех записей Task-ов
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Cоздание Task
    @Override
    public void addTask(Task task) {
        task.setId(idGen.next());
        tasks.putIfAbsent(task.getId(), task);
    }

    // Обновление Task
    @Override
    public void updateTask(Task task) {
        // task.getId() не может вернуть null, возвращает int
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    // Удаление Task
    @Override
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
    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // Получение всех записей Subtask-ов
    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Возвращаем вписок Subtasks по id эпика
    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return subtasks.values()
                .stream()
                .filter(s -> s.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    // Обновление Subtask
    @Override
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            // Обновляем статус
            updateEpicStatus(subtask.getEpicId());
        }
    }

    // Удаление Subtask
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = getSubtask(id).getEpicId();
            // Отвязываем Subtask от Epic (удалить id сабтасков из списка в эпике)
            getEpic(epicId).getSubtaskIds().remove(id);
            // Удаляем Subtask
            subtasks.remove(id);
            // Обновляем статус у Epic
            updateEpicStatus(epicId);
        }
    }

    // Удалить все Subtask
    @Override
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
    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    // Получение всех записей Epic-ов
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Cоздание Epic
    @Override
    public void addEpic(Epic epic) {
        epic.setId(idGen.next());
        epics.putIfAbsent(epic.getId(), epic);
    }

    // Создаем Subtask и добавляем его к эпику
    // Ваше замечание ... не должно быть возможности создать сабтаск без эпика
    // addSubtask был приватным и использовался только в этом методе (удалил метод)
    // Во время добавления сабтаска к эпику, сабтаск не имеет id эпика
    @Override
    public void addSubtaskToEpic(int epicId, String subtaskTitle, String subtaskDescription) {
        if (epics.containsKey(epicId)) {
            // Создаем Subtask
            Subtask subtask = new Subtask(epicId, subtaskTitle, subtaskDescription);
            subtask.setId(idGen.next());
            subtasks.put(subtask.getId(), subtask);
            // Привязываем к epic
            epics.get(epicId).add(subtask);
            // Обновляем статус
            updateEpicStatus(epicId);
        }
    }

    // Обновление Epic
    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // Удаление Epic
    @Override
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
    @Override
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

    @Override
    public List<BaseTask> getHistory() {
        return null;
    }

    private void addToHistory(int id){

    }
}
