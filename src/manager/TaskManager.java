package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);


    void deleteAllTasks();

    void deleteTaskById(int id);

    void deleteAllEpics();

    void deleteEpicById(int id);

    void deleteAllSubtasks();

    void deleteSubtaskById(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    Task findTaskById(int id);

    Epic findEpicById(int id);

    Subtask findSubtaskById(int id);

    List<Integer> findSubtasksByEpic(Epic epic);

    List<Task> getEpicSubtasks(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean checkCrossTime(Task t1, Task t2);
}