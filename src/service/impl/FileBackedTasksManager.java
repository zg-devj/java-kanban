package service.impl;

import exceptions.ManagerSaveException;
import model.BaseTask;
import model.Epic;
import model.Subtask;
import model.Task;
import util.TaskType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task t = super.getTask(id);
        save();
        return t;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic e = super.getEpic(id);
        save();
        return e;
    }

    @Override
    public void addSubtaskToEpic(int epicId, String subtaskTitle, String subtaskDescription) {
        super.addSubtaskToEpic(epicId, subtaskTitle, subtaskDescription);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask st = super.getSubtask(id);
        save();
        return st;
    }

    // преобразуем таск в строку
    private String taskToString(BaseTask task) {
        if (task instanceof Task) {
            return String.format("%d,%s,%s,%s,%s%n",
                    task.getId(), TaskType.TASK.name(), task.getTitle(),
                    task.getStatus().name(), task.getDescriptions());
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d%n",
                    subtask.getId(), TaskType.SUBTASK.name(), subtask.getTitle(),
                    subtask.getStatus().name(), subtask.getDescriptions(),
                    subtask.getEpicId());
        } else {
            return String.format("%d,%s,%s,%s,%s%n",
                    task.getId(), TaskType.EPIC.name(), task.getTitle(),
                    task.getStatus().name(), task.getDescriptions());
        }

    }

    // преобразуем строку в таск
    private BaseTask taskFromString(String value) {
        return null;
    }

    // преобразуем историю в строку
    private String historyToString() {
        List<String> ret = new ArrayList<>();
        for (BaseTask task : super.getHistory()) {
            ret.add(String.valueOf(task.getId()));
        }
        return String.join(",", ret);

    }

    // преобразуем строку в историю
    private List<Integer> historyFromString(String value) {
        return null;
    }

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
            // сохроняем таски
            for (Task task : getAllTasks()) {
                fileWriter.write(taskToString(task));
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(taskToString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(taskToString(subtask));
            }
            // сохроняем таски
            if (super.getHistory().size() > 0) {
                fileWriter.write(" \n");
                fileWriter.write(historyToString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager backed = new FileBackedTasksManager(file);
        // Если файл существует загружаем данные из файла
        if (file.exists()) {

        }
        return backed;
    }
}
