package service.impl;

import model.*;
import service.HistoryManager;
import service.TaskManager;
import util.DateTimeConverter;
import util.Identifier;
import util.Managers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final static int SECONDS_IN_MINUTE = 60;
    // Идентификаторы для Task, Subtask, Epic
    protected Identifier idGen;

    // история просмотров
    private HistoryManager historyManager;

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public InMemoryTaskManager() {
        this.idGen = new Identifier();
        this.historyManager = Managers.getDefaultHistory();
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    // возвращаем историю
    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    protected HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    protected HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    //region Task методы
    // Получение Task по идентификатору.
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            this.historyManager.add(task);
        }
        return tasks.get(id);
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
        tasks.putIfAbsent(task.getId(), task);
        return task.getId();
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
            // удаляем из истории
            historyManager.remove(id);
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
    // Ваше замечание ... не должно быть возможности создать сабтаск без эпика
    // addSubtask был приватным и использовался только в этом методе (удалил метод)
    // Во время добавления сабтаска к эпику, сабтаск не имеет id эпика
    @Override
    public int addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            // Создаем Subtask
            subtask.setId(idGen.next());
            subtasks.put(subtask.getId(), subtask);
            // Привязываем к epic
            epics.get(subtask.getEpicId()).add(subtask);
            // Обновляем статус
            updateEpicStatus(subtask.getEpicId());
            updateEpicTimeInterval(subtask.getEpicId());
            return subtask.getId();
        }
        return subtask.getId();
    }

    // Обновление Subtask
    @Override
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            if (subtasks.containsKey(subtask.getId())) {
                subtasks.put(subtask.getId(), subtask);
                // Обновляем статус
                updateEpicStatus(subtask.getEpicId());
                updateEpicTimeInterval(subtask.getEpicId());
            }
        }
    }

    // Удаление Subtask
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            // Отвязываем Subtask от Epic (удалить id сабтасков из списка в эпике)
            epics.get(epicId).getSubtaskIds().remove(id);
            // удаляем из истории
            historyManager.remove(id);
            // Удаляем Subtask
            subtasks.remove(id);
            // Обновляем статус у Epic
            updateEpicStatus(epicId);
            updateEpicTimeInterval(epicId);
        }
    }

    // Удалить все Subtask
    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : getAllSubtasks()) {
            // удалить id сабтасков из списка в эпике
            deleteSubtask(subtask.getId());
            // Обновляем статус
            // TODO: 09.01.2023 Delete, вызывается в deleteSubtask(subtask.getId());
            //updateEpicStatus(subtask.getEpicId());
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
        return epic;
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
            // удаляем эпик из истории
            historyManager.remove(id);
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

    // обновляем данные для Эпика по временным интервалам
    private void updateEpicTimeInterval(int epicId) {
        Epic epic = epics.get(epicId);
        epic.setEndTime(null);
        List<Subtask> subtaskList = getSubtasksByEpicId(epicId);
        Long durationMinute = 0L;
        Instant instantFirst = null;
        Instant instantLast = null;
        Long durationLast = 0L;
        for (Subtask subtask : subtaskList) {
            // TODO: 10.01.2023 Delete 
//            System.out.println(
//                    DateTimeConverter.fromInstantToString(subtask.getStartTime())
//                            + " : "
//                            + subtask.getDurationMinute() + " : "
//                            + DateTimeConverter.fromInstantToString(subtask.getEndTime())
//            );
            if (subtask.getDurationMinute() > 0L) {
                durationMinute += subtask.getDurationMinute();
            }
            if (subtask.getStartTime() != null) {
                if (instantFirst != null) {
                    if (subtask.getStartTime().isBefore(instantFirst)) {
                        instantFirst = subtask.getStartTime();
                    }
                } else {
                    instantFirst = subtask.getStartTime();
                }
                if (instantLast != null) {
                    if (subtask.getStartTime().isAfter(instantLast)) {
                        instantLast = subtask.getStartTime();
                        durationLast = subtask.getDurationMinute();
                    }
                } else {
                    instantLast = subtask.getStartTime();
                    durationLast = subtask.getDurationMinute();
                }
            }
        }
        epic.setDurationMinute(durationMinute);
        epic.setStartTime(instantFirst);
        if (instantLast != null) {
            epic.setEndTime(instantLast.plusSeconds(durationLast * SECONDS_IN_MINUTE));
        }
        // TODO: 10.01.2023 Delete
//        System.out.println("Epic: \nduration=" + epic.getDurationMinute()
//                + "\n startTime=" + DateTimeConverter.fromInstantToString(epic.getStartTime())
//                + "\n endTime=" + DateTimeConverter.fromInstantToString(epic.getEndTime()));
    }

    @Override
    public List<BaseTask> getHistory() {
        return this.historyManager.getHistory();
    }
}
