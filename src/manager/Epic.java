package manager;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {


    protected ArrayList<Integer> idSubtaskEpics = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.id = null;
    }

    public Epic(int id, String name, String description) {
        super(name, description, Status.NEW);
        this.id = id;
    }

    public ArrayList<Integer> getIdSubtaskEpics() {
        return idSubtaskEpics;
    }


    public void addSubtaskIdToEpics(int SubtaskId) {
        //System.out.println(idSubtaskEpics.equals(SubtaskId));
        if (!idSubtaskEpics.contains(SubtaskId) && SubtaskId != getId()) {
            idSubtaskEpics.add(SubtaskId);
        }
    }

}