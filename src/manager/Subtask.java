package manager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected Integer epicId;


    public Integer getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Status status, Integer epicId, Duration duration, LocalDateTime localDateTime) {
        super(name, description, status,duration, localDateTime);
        this.epicId = epicId;
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

    public Subtask(Integer id, String name, String description, Status status, Integer epicId, Duration duration, LocalDateTime localDateTime) {
        super(id, name, description, status,duration, localDateTime);
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}