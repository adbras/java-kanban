import java.util.ArrayList;

public class Epic extends Task {


    protected ArrayList<Integer> idSubtaskEpics = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.id = null;
    }

    public ArrayList<Integer> getIdSubtaskEpics() {
        return idSubtaskEpics;
    }


    public void addSubtaskIdToEpics (int SubtaskId) {
        idSubtaskEpics.add(SubtaskId);
    }

}
