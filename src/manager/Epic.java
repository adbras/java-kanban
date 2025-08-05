package manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {


    protected ArrayList<Integer> idSubtaskEpics = new ArrayList<>();

    public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, Status.NEW, duration, startTime);
        this.id = null;
    }

    public Epic(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, Status.NEW, duration, startTime);
        this.id = id;
    }

    public ArrayList<Integer> getIdSubtaskEpics() {
        return idSubtaskEpics;
    }


    public void addSubtaskIdToEpics(int subtaskId) {
        //System.out.println(idSubtaskEpics.equals(SubtaskId));
        if (!idSubtaskEpics.contains(subtaskId) && subtaskId != getId()) {
            idSubtaskEpics.add(subtaskId);
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

}