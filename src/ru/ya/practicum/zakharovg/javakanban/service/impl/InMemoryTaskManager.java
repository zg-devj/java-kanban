package ru.ya.practicum.zakharovg.javakanban.service.impl;

import ru.ya.practicum.zakharovg.javakanban.exceptions.OutOfTimeIntervalException;
import ru.ya.practicum.zakharovg.javakanban.model.*;
import ru.ya.practicum.zakharovg.javakanban.service.HistoryManager;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Identifier;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.javakanban.util.SortedBaseTask;
import ru.ya.practicum.zakharovg.javakanban.util.TaskComparator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final static int SECONDS_IN_MINUTE = 60;
    // Идентификаторы для Task, Subtask, Epic
    protected Identifier idGen;

    // история просмотров
    protected HistoryManager historyManager;

    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;

    protected SortedBaseTask sortedTasks;



    public InMemoryTaskManager() {
        this.idGen = new Identifier();
        this.historyManager = Managers.getDefaultHistory();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.sortedTasks = new SortedBaseTask();
    }

    //region Task методы
    // Получение Task по идентификатору.
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            this.historyManager.add(task);
        }
        return task;
    }

    // Получение всех записей Task-ов
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Cоздание Task
    @Override
    public int addTask(Task task) {
        task.setId(idGen.next());
        if (sortedTasks.validate(task)) {
            tasks.putIfAbsent(task.getId(), task);
            // добавляем в отсортированный список
            sortedTasks.add(task);
        } else {
            throw new OutOfTimeIntervalException("Добавляемая задача пересекается с существующими");
        }
        return task.getId();
    }

    // Обновление Task
    @Override
    public void updateTask(Task task) {
        // task.getId() не может вернуть null, возвращает int
        if (task != null && tasks.containsKey(task.getId())) {

            if (sortedTasks.validate(task)) {
                sortedTasks.remove(task);
                tasks.put(task.getId(), task);
                sortedTasks.add(task);
            } else {
                throw new OutOfTimeIntervalException("Добавляемая задача пересекается с существующими");
            }

        }
    }

    // Удаление Task
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            // удаляем из истории
            historyManager.remove(id);
            // добавляем из отсортированного списка
            sortedTasks.remove(task);
            // удаляем Task
            tasks.remove(id);
        }
    }

    // Удаление все Tasks
    public void deleteAllTasks() {
        for (Task task : getAllTasks()) {
            deleteTask(task.getId());
        }
    }
    //endregion

    //region Subtask методы
    // Получение Subtask по идентификатору.
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            this.historyManager.add(subtask);
        }
        return subtask;
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

    // Создаем Subtask и добавляем его к эпику
    // Вынес epicId из Subtask в отдельное поле
    @Override
    public int addSubtask(int epicId, Subtask subtask) {
        if (epics.containsKey(epicId)) {
            subtask.setId(idGen.next());
            if (sortedTasks.validate(subtask)) {
                subtask.setEpicId(epicId);
                // Создаем Subtask
                subtasks.put(subtask.getId(), subtask);
                // добавляем в отсортированный список
                sortedTasks.add(subtask);
                // Привязываем к epic
                epics.get(subtask.getEpicId()).add(subtask);
                // Обновляем статус
                updateEpicState(subtask.getEpicId());
            } else {
                throw new OutOfTimeIntervalException("Добавляемая задача пересекается с существующими");
            }
        }
        return subtask.getId();
    }

    // Обновление Subtask
    @Override
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId()) && subtasks.containsKey(subtask.getId())) {
            if (sortedTasks.validate(subtask)) {
                sortedTasks.remove(subtask);
                subtasks.put(subtask.getId(), subtask);
                sortedTasks.add(subtask);
                // Обновляем статус
                updateEpicState(subtask.getEpicId());
            } else {
                throw new OutOfTimeIntervalException("Добавляемая задача пересекается с существующими");
            }
        }
    }

    // Удаление Subtask
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int epicId = subtask.getEpicId();
            // Отвязываем Subtask от Epic (удалить id сабтасков из списка в эпике)
            epics.get(epicId).getSubtaskIds().remove(id);
            // удаляем из истории
            historyManager.remove(id);
            // удаляем из отсортированного списка
            sortedTasks.remove(subtask);
            // Удаляем Subtask
            subtasks.remove(id);
            // Обновляем статус у Epic
            updateEpicState(epicId);
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
        Epic epic = epics.get(id);
        if (epic != null) {
            this.historyManager.add(epic);
        }
        return epics.get(id);
    }

    // Получение всех записей Epic-ов
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Cоздание Epic
    @Override
    public int addEpic(Epic epic) {
        epic.setId(idGen.next());
        epics.putIfAbsent(epic.getId(), epic);
        return epic.getId();
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
        // Возвращаю все сабтаски эпика,
        // удаляю сабтаски,
        // удаляю эпик, из сета ничего не удаляется
        if (epics.containsKey(id)) {
            Integer[] list = epics.get(id).getSubtaskIds().toArray(new Integer[0]);
            for (Integer delId : list) {
                // Удаляем сабтаски эпика
                deleteSubtask(delId);
            }
            // удаляем эпик из истории
            historyManager.remove(id);
            // Удаляем Epic
            epics.remove(id);
        }
    }

    // Удалить все Epic
    @Override
    public void deleteAllEpics() {
        for (Subtask subtask : getAllSubtasks()) {
            historyManager.remove(subtask.getId());
        }
        for (Epic epic : getAllEpics()) {
            historyManager.remove(epic.getId());
        }
        subtasks.clear();
        epics.clear();
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

    // обновляем данные для Эпика по временным интервалам
    protected void updateEpicTimeInterval(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubtaskIds().size() > 0) {
            List<Subtask> subtaskList = getSubtasksByEpicId(epicId);
            Collections.sort(subtaskList, new TaskComparator());
            epic.setStartTime(subtaskList.get(0).getStartTime());
            epic.setDurationMinute(
                    subtaskList.stream().mapToLong(i -> i.getDurationMinute()).sum()
            );
            // последняя подзадача эпика с startTime != null
            Subtask lastSubtask = subtaskList.stream()
                    .filter(t -> t.getStartTime() != null)
                    .reduce((f, s) -> s).orElse(null);
            if (lastSubtask != null) {
                // устанавливаем время окончания эпика
                epic.setEndTime(lastSubtask.getEndTime());
            }
        } else {
            epic.setStartTime((Instant) null);
            epic.setDurationMinute(0);
            epic.setEndTime(null);
        }
    }

    private void updateEpicState(int epicId) {
        updateEpicStatus(epicId);
        updateEpicTimeInterval(epicId);
    }

    @Override
    public List<BaseTask> getHistory() {
        return this.historyManager.getHistory();
    }
}
