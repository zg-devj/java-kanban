package service.impl;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import model.*;
import service.HistoryManager;
import util.TaskConverter;
import util.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    private FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private void addItemToTaskList(Task task) {
        getTasks().put(task.getId(), task);
    }

    private void addItemToEpicList(Epic epic) {
        getEpics().put(epic.getId(), epic);
    }

    private void addItemToSubtaskList(Subtask subtask) {
        getSubtasks().put(subtask.getId(), subtask);
    }

    //region Task
    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save();
        return t;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
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
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtaskToEpic(int epicId, String subtaskTitle, String subtaskDescription) {
        super.addSubtaskToEpic(epicId, subtaskTitle, subtaskDescription);
        save();
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

    // загрузка данных из файла
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager backed = new FileBackedTasksManager(file);
        // Если файл существует загружаем данные из файла
        if (file.exists()) {
            boolean isHistory = false;
            // читаем файл
            try (FileReader reader = new FileReader(file);
                 BufferedReader br = new BufferedReader(reader)) {
                while (br.ready()) {
                    String line = br.readLine();
                    if (line.isBlank() || line.isEmpty()) {
                        isHistory = true;
                        continue;
                    }
                    if (!isHistory) {
                        // если задача
                        BaseTask task = TaskConverter.taskFromString(line);
                        if (task instanceof Task) {
                            backed.addItemToTaskList((Task) task);
                        } else if (task instanceof Epic) {
                            backed.addItemToEpicList((Epic) task);
                        } else if (task instanceof Subtask) {
                            backed.addItemToSubtaskList((Subtask) task);
                        }
                    } else {
                        // если история
                        List<Integer> list = TaskConverter.historyFromString(line);
                        if (list != null) {
                            HistoryManager historyManager = backed.getHistoryManager();
                            for (Integer unit : list) {
                                if (backed.getTasks().containsKey(unit)) {
                                    historyManager.add(backed.getTasks().get(unit));
                                } else if (backed.getEpics().containsKey(unit)) {
                                    historyManager.add(backed.getEpics().get(unit));
                                } else if (backed.getSubtasks().containsKey(unit)) {
                                    historyManager.add(backed.getSubtasks().get(unit));
                                }
                            }
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
