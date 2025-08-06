package tasks;

import manager.Status;
import manager.TaskType;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected Integer id;
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    protected String taskName;
    protected String description;
    protected Status status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String taskName, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.id = null;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Integer id, String taskName, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);  // возвращаем хэш по id
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // если ссылки одинаковые
        if (o == null || getClass() != o.getClass()) return false;  // если объекты разных типов
        Task task = (Task) o;  // приводим объект к типу Task
        return Objects.equals(id, task.id);  // сравниваем id
    }
}
