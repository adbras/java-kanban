import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();

    int taskID = 0;


    void addNewTask(Task task) {
        taskID++;
        task.setId(taskID);
        tasks.put(task.getId(), task);
        System.out.println("Task added");
    }

    void addNewEpic(Epic epic) {
        taskID++;
        epic.setId(taskID);
        epics.put(epic.getId(), epic);
        System.out.println("Epic added");
    }

    void addNewSubtask(Subtask subtask) {
        taskID++;
        subtask.setId(taskID);
        subTasks.put(subtask.getId(), subtask);
        System.out.println("Subtask added");
    }


    void printAllTasks() {
        System.out.println("Вывод всех задач!");
        System.out.println(tasks.toString());
        System.out.println(epics.toString());
        System.out.println(subTasks.toString());
    }

    void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        System.out.println("Все задачи удалены!");
    }

    void findTaskNameById(int Id) {
        if (Id < 0) {
            System.out.println("Минусовой Id не существует");
            return;
        }

        if (tasks.get(Id) != null) {
            System.out.print("Найдена задача: ");
            System.out.println(tasks.get(Id).toString());
        } else if (epics.get(Id) != null) {
            System.out.print("Найден эпик:  ");
            System.out.println(epics.get(Id).toString());
        } else if (subTasks.get(Id) != null) {
            System.out.print("Найдена подзадача: ");
            System.out.println(subTasks.get(Id).toString());
        } else {
            System.out.println("Задачи/подзадачи/эпиков с таким Id не найдено!");
        }
    }

    void findSubtasksByEpic(Epic epic) {
        System.out.println("Подзадачи эпика: ");
        for (Subtask subtask : subTasks.values()) {
            if (subtask.epicId.equals(epic)) {
                System.out.println(subtask.toString());
            }
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        System.out.println(tasks);
    }

    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        Epic epic = subtask.epicId;
        updateEpic(epic);
    }

    public void updateEpic(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;
        for (Subtask subtask : subTasks.values()) {
            if (subtask.epicId.equals(epic)) {
                if (subtask.status != Status.DONE) {
                    allDone = false;
                }
                if (subtask.status != Status.NEW) {
                    allNew = false;
                }
            }
        }

        // Устанавливаем статус эпика
        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        System.out.println("Статус эпика обновлен: " + epic.getStatus());

    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            System.out.println("Задача с ID " + id + " удалена.");
        } else if (epics.containsKey(id)) {
            epics.remove(id);
            System.out.println("Эпик с ID " + id + " удален.");
        } else if (subTasks.containsKey(id)) {
            subTasks.remove(id);
            System.out.println("Подзадача с ID " + id + " удалена.");
        } else {
            System.out.println("Задача/эпик/подзадача с таким ID не найдена.");
        }
    }
}