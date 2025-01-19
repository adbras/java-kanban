package manager;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();
    void addTask (Task task);
}
