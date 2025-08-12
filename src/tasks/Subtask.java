package tasks;

import manager.Status;
import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Status status, Integer epicId, Duration duration, LocalDateTime localDateTime) {
        super(name, description, status, duration, localDateTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer epicId, Duration duration, LocalDateTime localDateTime) {
        super(id, name, description, status, duration, localDateTime);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}