package manager;
import java.util.List;

public interface HistoryManager {

    void remove(int id);
    List<Task> getHistory();
    void addTask (Task task);
}
