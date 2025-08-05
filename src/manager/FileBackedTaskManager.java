package manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic,duration, startTime \n");

        for (Task task : getTasks()) {
            sb.append(toString(task)).append("\n");
        }

        for (Epic epic : getEpics()) {
            sb.append(toString(epic)).append("\n");
        }

        for (Subtask subtask : getSubtasks()) {
            sb.append(toString(subtask)).append("\n");
        }

        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения менеджера в файл", e);
        }
    }

    private String toString(Task task) {
        switch (task.getTaskType()) {
            case TASK:
                return String.format("%d,TASK,%s,%s,%s,%s,%s,", task.getId(), task.getTaskName(), task.getStatus(), task.getDescription(), task.getDuration(), task.getStartTime());
            case EPIC:
                return String.format("%d,EPIC,%s,%s,%s,%s,%s,", task.getId(), task.getTaskName(), task.getStatus(), task.getDescription(), task.getDuration(), task.getStartTime());
            case SUBTASK:
                return String.format("%d,SUBTASK,%s,%s,%s,%s,%s,%d,", task.getId(), task.getTaskName(), task.getStatus(), task.getDescription(), task.getDuration(), task.getStartTime(), ((Subtask) task).getEpicId());
            default:
                throw new IllegalArgumentException("Unknown task type");
        }
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        String statusBeforeEnum = fields[3];
        Status status = Status.valueOf(statusBeforeEnum);
        String description = fields[4];
        LocalDateTime startTime = LocalDateTime.parse(fields[6], DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[7]));

        Task task;
        switch (type) {
            case "TASK":
                task = new Task(id, name, description, status, duration, startTime);
                break;
            case "EPIC":
                task = new Epic(id, name, description, duration, startTime);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(fields[7]);
                task = new Subtask(id, name, description, status, epicId, duration, startTime);
                task.setId(id);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                switch (task.getTaskType()) {
                    case TASK -> manager.addNewTask(task);
                    case SUBTASK -> manager.addNewSubtask((Subtask) task);
                    case EPIC -> manager.addNewEpic((Epic) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки менеджера из файла", e);
        }
        return manager;
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}