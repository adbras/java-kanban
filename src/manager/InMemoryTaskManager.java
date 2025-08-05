package manager;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskID = 0;
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(Task::getId);
    private final Map<Task, String> tasksSortedByTime = new TreeMap<>(taskComparator);
    ;


    @Override
    public int addNewTask(Task task) {
        if (task.getId() == null) {
            taskID++;
            task.setId(taskID);
        }
        tasks.put(task.getId(), task);
        addTaskToSortedMap(task);
        return taskID;
    }

    @Override
    public int addNewEpic(Epic epic) {
        if (epic.getId() == null) {
            taskID++;
            epic.setId(taskID);
        }

        epics.put(epic.getId(), epic);
        addTaskToSortedMap(epic);
        return taskID;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (subtask.getId() == null) {
            taskID++;
            subtask.setId(taskID);
        }
        subTasks.put(subtask.getId(), subtask);
        System.out.println("Subtask added");

        Epic epic = epics.get(subtask.getEpicId());
        if (epic.getId() != taskID) {
            epic.addSubtaskIdToEpics(taskID);
            updateEpicStatus(epic);
        }
        addTaskToSortedMap(subtask);
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
        tasksSortedByTime.clear();
        System.out.println("Все Таски удалены!");
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        tasksSortedByTime.remove(task);
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
            Task task = subTasks.remove(i);
            tasksSortedByTime.remove(task);
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
        return new ArrayList<Task>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subTasks.values());
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
        addTaskToSortedMap(task);
        System.out.println(tasks);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.epicId));

        addTaskToSortedMap(subtask);
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

    @Override
    public List<Task> getPrioritizedTasks() {

        List<Task> sortedTaskList = new ArrayList<>();

        for (Map.Entry<Task, String> entry : tasksSortedByTime.entrySet()) {
            sortedTaskList.addLast(entry.getKey());
        }

        return sortedTaskList;
    }

    private void addTaskToSortedMap(Task task) {
        if (task.getStartTime() == null) {
            return;
        }

        LocalDateTime curentTime = LocalDateTime.now();
        if (tasksSortedByTime.isEmpty()) {
            tasksSortedByTime.put(task, curentTime.format(Task.DATE_TIME_FORMATTER));
            return;
        }

        List<Task> crossTime = getPrioritizedTasks().stream()
                .filter((Task existsTask) -> !checkCrossTime(task, existsTask))
                .toList();

        if (crossTime.isEmpty()) {
            tasksSortedByTime.put(task, curentTime.format(Task.DATE_TIME_FORMATTER));
        } else {
            System.out.println("Конфликт по времени \n " + task.toString());
        }
    }

    @Override
    public boolean checkCrossTime(Task task1, Task task2) {
        if (task1.equals(task2)) {
            return true;
        }
        LocalDateTime task1Start = task1.getStartTime();
        LocalDateTime task1End = task1.getEndTime();
        LocalDateTime task2Start = task2.getStartTime();
        LocalDateTime task2End = task2.getEndTime();

        return (task2Start.isBefore(task1Start) && task2End.isBefore(task1Start)) ||
                task2Start.isAfter(task1End);
    }
}