package ru.ya.practicum.zakharovg.javakanban.service.impl;

import ru.ya.practicum.zakharovg.javakanban.exceptions.ManagerLoadException;
import ru.ya.practicum.zakharovg.javakanban.exceptions.ManagerSaveException;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.HistoryManager;
import ru.ya.practicum.zakharovg.javakanban.util.SortedBaseTask;
import ru.ya.practicum.zakharovg.javakanban.util.TaskConverter;
import ru.ya.practicum.zakharovg.javakanban.util.TaskType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    private FileBackedTasksManager(Path path) {
        super();
        this.file = path.toFile();
    }

    private void addItemToTaskList(Task task) {
        tasks.put(task.getId(), task);
        idGen.setMaxId(task.getId());
        sortedTasks.add(task);
    }

    private void addItemToEpicList(Epic epic) {
        epics.put(epic.getId(), epic);
        idGen.setMaxId(epic.getId());
    }

    private void addItemToSubtaskList(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        idGen.setMaxId(subtask.getId());
        sortedTasks.add(subtask);
        updateEpicTimeInterval(subtask.getEpicId());
    }

    //region Task
    // метод save() из ТЗ-6:
    // - "Создайте метод save без параметров — он будет сохранять
    // текущее состояние менеджера в указанный файл."
    // - "Что он должен сохранять? Все задачи, подзадачи,
    // эпики и историю просмотра любых задач."
    //  - метод getTask, getEpic, getSubtask изменяют
    //  историю просмотра из условий ТЗ-5
    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save();
        return t;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }
    //endregion

    //region Subtask
    @Override
    public Subtask getSubtask(int id) {
        Subtask st = super.getSubtask(id);
        save();
        return st;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }
    //endregion

    //region Epic
    @Override
    public Epic getEpic(int id) {
        Epic ep = super.getEpic(id);
        save();
        return ep;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    //
    @Override
    public int addSubtask(int epicId, Subtask subtask) {
        int id = super.addSubtask(epicId, subtask);
        save();
        return id;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }
    //endregion

    // Сохраняем состояние задач и истории
    private void save() {
        if (!file.exists()) {
            // создаем файл, если не существует
            try {
                Files.createFile(file.toPath());
            } catch (IOException ex) {
                throw new ManagerSaveException();
            }
        }

        try (Writer fileWriter = new FileWriter(file);) {
            // сохраняем таски
            for (Task task : getAllTasks()) {
                fileWriter.write(TaskConverter.taskToString(task));
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(TaskConverter.taskToString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(TaskConverter.taskToString(subtask));
            }
            // сохраняем историю
            if (super.getHistory().size() > 0) {
                fileWriter.write(" \n");
                fileWriter.write(TaskConverter.historyToString(super.getHistory()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

    }

    private void addItemToLists(FileBackedTasksManager backed, BaseTask task, TaskType type) {
        switch (type) {
            case TASK:
                backed.addItemToTaskList((Task) task);
                break;
            case EPIC:
                backed.addItemToEpicList((Epic) task);
                break;
            case SUBTASK:
                backed.addItemToSubtaskList((Subtask) task);
                break;
        }
    }

    // загрузка данных из файла
    public static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager backed = new FileBackedTasksManager(path);
        if (Files.exists(path)) {
            boolean isHistory = false;
            try {
                String data = Files.readString(path);
                String[] lines = data.split("\n");
                List<Integer> listHistory = null;
                for (String line : lines) {
                    if (line.isBlank() || line.isEmpty()) {
                        isHistory = true;
                        continue;
                    }
                    if (!isHistory) {
                        // если задача
                        String[] taskLine = line.split(",");
                        BaseTask task = TaskConverter.taskFromString(taskLine);
                        TaskType taskType = TaskType.valueOf(taskLine[1]);
                        backed.addItemToLists(backed, task, taskType);
                    } else {
                        // если история
                        listHistory = TaskConverter.historyFromString(line);
                    }
                }
                if (listHistory != null) {
                    HistoryManager historyManager = backed.historyManager;
                    for (Integer unit : listHistory) {
                        if (backed.tasks.containsKey(unit)) {
                            historyManager.add(backed.tasks.get(unit));
                        } else if (backed.epics.containsKey(unit)) {
                            historyManager.add(backed.epics.get(unit));
                        } else if (backed.subtasks.containsKey(unit)) {
                            historyManager.add(backed.subtasks.get(unit));
                        }
                    }
                }
            } catch (IOException ex) {
                throw new ManagerLoadException();
            }
        }
        return backed;
    }
}
