public class Subtask extends Task {
    Epic epicId;


    public Subtask(String name, String description, Status status, Epic epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId.taskName +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public Subtask(Integer id, String name, String description, Status status, Epic epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

}
