package service.impl;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import model.*;
import service.HistoryManager;
import util.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
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
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            return String.format("%d,%s,%s,%s,%s,%s%n",
                    epic.getId(), TaskType.EPIC.name(), epic.getTitle(),
                    epic.getStatus().name(), epic.getDescriptions(),
                    getSubtasksIdToString(epic.getSubtaskIds()));
        }
        return null;
    }

    // преобразуем в строку ссылки эпика на сабтаски
    // пример:
    // return: 2:3:4  -> эпик ссылается на сабтаски c id: 2,3,4
    private String getSubtasksIdToString(HashSet<Integer> list) {
        if (list.size() > 0) {
            List<String> ret = new ArrayList<>();
            for (Integer id : new ArrayList<>(list)) {
                ret.add(String.valueOf(id));
            }
            return String.join(":", ret);
        } else {
            // если у эпика еще нет сабтасков
            return "0";
        }
    }

    // преобразуем строку в таск
    private BaseTask taskFromString(String value) {
        String[] taskLine = value.split(",");
        Integer id = Integer.valueOf(taskLine[0]);
        TaskType type = TaskType.valueOf(taskLine[1]);
        String title = taskLine[2].toString();
        Status status = Status.valueOf(taskLine[3]);
        String desc = taskLine[4].toString();
        switch (type) {
            case TASK:
                Task task = new Task(title, desc);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                String[] subtasks = taskLine[5].split(":");
                Epic epic = new Epic(title, desc);
                epic.setId(id);
                epic.setStatus(status);
                if (Integer.valueOf(subtasks[0]) != 0) {
                    for (String unit : subtasks) {
                        epic.add(Integer.valueOf(unit));
                    }
                }
                return epic;
            case SUBTASK:
                Integer epicId = Integer.valueOf(taskLine[5]);
                Subtask subtask = new Subtask(epicId, title, desc);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
        }
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
        String[] historyArr = value.split(",");
        if (historyArr.length > 0) {
            List<Integer> ret = new ArrayList<>();
            for (int i = 0; i < historyArr.length; i++) {
                ret.add(Integer.valueOf(historyArr[i]));
            }
            return ret;
        }
        return null;
    }

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
                        BaseTask task = backed.taskFromString(line);
                        if (task instanceof Task) {
                            backed.addItemToTaskList((Task) task);
                        } else if (task instanceof Epic) {
                            backed.addItemToEpicList((Epic) task);
                        } else if (task instanceof Subtask) {
                            backed.addItemToSubtaskList((Subtask) task);
                        }
                    } else {
                        System.out.println("Загружаем историю");
                        HistoryManager historyManager = backed.getHistoryManager();
                        List<Integer> list = backed.historyFromString(line);
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
            } catch (IOException ex) {
                throw new ManagerLoadException();
            }

        }
        return backed;
    }
}
