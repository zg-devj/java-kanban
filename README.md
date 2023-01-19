# java-kanban

Repository for homework project.

**Сервер:**
> src/ru/ya/practicum/zakharovg/taskserver/server/HttpTaskServer.java

## Эндпоинты

**Task**

|        |                          |                                     |
|:-------|:-------------------------|:------------------------------------|
| GET    | Task getTask(int)        | /tasks/task?id=                     | 
| GET    | List<Task> getAllTasks() | /tasks/task                         |
| POST   | int addTask(Task task)   | /tasks/task BODY: {task}            |
| PUT    | updateTask(Task task)    | /tasks/task BODY: {task}            |
| DELETE | deleteTask(int id)       | /tasks/task?id=                     |
| DELETE | deleteAllTasks()         | /tasks/task                         |

**Subtask**

|        |                                          |                                        |
|:-------|:-----------------------------------------|:---------------------------------------|
| GET    | Subtask getSubtask(int id)               | /tasks/subtask?id=                     |
| GET    | List<Subtask> getAllSubtasks()           | /tasks/subtask                         |
| POST   | int addSubtask(int epicId, Subtask task) | /tasks/subtask BODY: {subtask,,epicid} |
| PUT    | updateSubtask(Subtask subtask)           | /tasks/subtask BODY: {subtask}         |
| DELETE | deleteSubtask(int id)                    | /tasks/subtask?id=                     |
| DELETE | deleteAllSubtasks()                      | /tasks/subtask                         |

**Epic**

|        |                          |                          |
|:-------|:-------------------------|:-------------------------|
| GET    | Epic getEpic(int id)     | /tasks/epic?id=          |
| GET    | List<Epic> getAllEpics() | /tasks/epic              |
| POST   | int addEpic(Epic epic)   | /tasks/epic BODY: {epic} |
| PUT    | updateEpic(Epic epic)    | /tasks/epic BODY: {epic} |
| DELETE | deleteEpiv(Epic epic)    | /tasks/epic?id=          |
| DELETE | deleteAllEpics()         | /tasks/epic              |

**Другие**

|     |                                               |                         |
|:----|:----------------------------------------------|:------------------------|
| GET | List<Subtask> getSubtasksByEpicId(int epicId) | /tasks/subtask/epic?id= |
| GET | Set<BaseTask> getPrioritizedTasks()           | /tasks                  |
| GET | List<BaseTask> getHistory()                   | /tasks/history          |
