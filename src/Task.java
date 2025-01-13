public class Task {
    protected Integer id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
