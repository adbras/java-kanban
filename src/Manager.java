import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    int taskID = 0;


    int addNewTask(Task task) {
        taskID++;
        task.setId(taskID);
        tasks.put(task.getId(), task);
        System.out.println("Task added");
        return taskID;
    }

    int addNewEpic(Epic epic) {
        taskID++;
        epic.setId(taskID);
        epics.put(epic.getId(), epic);
        System.out.println("Epic added");
        return taskID;
    }

    int addNewSubtask(Subtask subtask) {
        taskID++;
        subtask.setId(taskID);
        subTasks.put(subtask.getId(), subtask);
        System.out.println("Subtask added");

        Epic epic = epics.get(subtask.epicId);
        epic.addSubtaskIdToEpics(taskID);
        updateEpicStatus(epic);
        return taskID;
    }


    void printAllTasks() {
        System.out.println("Вывод всех задач!");
        System.out.println(tasks.toString());
        System.out.println(epics.toString());
        System.out.println(subTasks.toString());
    }


    void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все Таски удалены!");
    }

    void deleteTaskById(int id) {
        tasks.remove(id);
        System.out.println("Таск по ID " + id + " удален!");
    }

    void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
        System.out.println("Все Эпики и сабтаски удалены!");
    }

    void deleteEpicById(int id) {
        ArrayList<Integer> idSubtasks = epics.get(id).getIdSubtaskEpics();
        for (int i : idSubtasks) {
            subTasks.remove(i);
        }
        epics.remove(id);

        System.out.println("Эпик по ID " + id + " удален! Так же удалены все его подзадачи");
    }

    void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
        System.out.println("Все сабтаски удалены! У всех эпиков статус Нью");
    }

    void deleteSubtaskById(int id) {
        int epicId = subTasks.get(id).epicId;
        updateEpicStatus(epics.get(epicId));
        subTasks.remove(id);
        System.out.println("Сабтаск удален, статус эпика обновлен!");
    }


    public ArrayList<Task> getTasks() {
        ArrayList<Task> task = new ArrayList<Task>(tasks.values());
        return task;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epic = new ArrayList<Epic>(epics.values());
        return epic;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtask = new ArrayList<Subtask>(subTasks.values());
        return subtask;
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public Epic findEpicById(int id) {
        return epics.get(id);
    }

    public Subtask findSubtaskById(int id) {
        return subTasks.get(id);
    }


    public ArrayList<Integer> findSubtasksByEpic(Epic epic) {
        ArrayList<Integer> subtasksList = new ArrayList<>(epic.getIdSubtaskEpics());
        return subtasksList;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        System.out.println(tasks);
    }

    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.epicId));
    }

    public void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;

        ArrayList<Integer> idSubtasksEpics = epic.getIdSubtaskEpics();
        System.out.println(idSubtasksEpics);
        for (Integer i : idSubtasksEpics) {
            if (subTasks.get(i).getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subTasks.get(i).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (!allDone && !allNew) {
                break;
            }
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        System.out.println("Статус эпика обновлен: " + epic.getStatus());

    }
}