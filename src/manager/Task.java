package manager;
import java.util.Objects;

public class Task {
    protected Integer id;

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    protected String taskName;
    protected String description;
    protected Status status;

    public Task(String taskName, String description, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.id = null;
    }

    public Task(Integer id, String taskName, String description, Status status) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public TaskType getTaskType() {
        return TaskType.TASK;
    }
}
