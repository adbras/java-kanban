package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskID = 0;


    @Override
    public int addNewTask(Task task) {
        taskID++;
        task.setId(taskID);
        tasks.put(task.getId(), task);
        System.out.println("com.Task added");
        return taskID;
    }

    @Override
    public int addNewEpic(Epic epic) {
        taskID++;
        epic.setId(taskID);
        epics.put(epic.getId(), epic);
        System.out.println("com.Epic added");
        return taskID;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        taskID++;
        subtask.setId(taskID);
        subTasks.put(subtask.getId(), subtask);
        System.out.println("Subtask added");

        Epic epic = epics.get(subtask.epicId);
        if (epic.getId() != taskID) {
            epic.addSubtaskIdToEpics(taskID);
            updateEpicStatus(epic);
        }
        return taskID;

    }

    @Override
    public ArrayList<Task> getEpicSubtasks(int taskId) {
        Epic epic = epics.get(taskId);
        ArrayList<Integer> idSub = epic.getIdSubtaskEpics();


        ArrayList<Task> subtasksList = new ArrayList<>();

        for (int i : idSub) {
            subtasksList.add(subTasks.get(i));
        }

        return subtasksList;
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);
            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }


    @Override
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все Таски удалены!");
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        System.out.println("Таск по ID " + id + " удален!");
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
        System.out.println("Все Эпики и сабтаски удалены!");
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Integer> idSubtasks = epics.get(id).getIdSubtaskEpics();
        for (int i : idSubtasks) {
            subTasks.remove(i);
        }
        epics.remove(id);

        System.out.println("Эпик по ID " + id + " удален! Так же удалены все его подзадачи");
    }

    @Override
    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
        System.out.println("Все сабтаски удалены! У всех эпиков статус Нью");
    }

    @Override
    public void deleteSubtaskById(int id) {
        int epicId = subTasks.get(id).epicId;
        updateEpicStatus(epics.get(epicId));
        subTasks.remove(id);
        System.out.println("Сабтаск удален, статус эпика обновлен!");
    }


    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> task = new ArrayList<Task>(tasks.values());
        return task;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epic = new ArrayList<Epic>(epics.values());
        return epic;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtask = new ArrayList<Subtask>(subTasks.values());
        return subtask;
    }

    @Override
    public Task findTaskById(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic findEpicById(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask findSubtaskById(int id) {
        historyManager.addTask(subTasks.get(id));
        return subTasks.get(id);
    }


    @Override
    public ArrayList<Integer> findSubtasksByEpic(Epic epic) {
        ArrayList<Integer> subtasksList = new ArrayList<>(epic.getIdSubtaskEpics());
        return subtasksList;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        System.out.println(tasks);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.epicId));
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}