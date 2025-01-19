package manager;

import java.util.ArrayList;

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

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    Task findTaskById(int id);

    Epic findEpicById(int id);

    Subtask findSubtaskById(int id);

    ArrayList<Integer> findSubtasksByEpic(Epic epic);
    ArrayList<Task>  getEpicSubtasks(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);

    ArrayList<Task> getHistory();
}
