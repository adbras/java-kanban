package manager;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyTasks = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public void addTask(Task task) {
        historyTasks.add(task);
        if (historyTasks.size() > 10) {
            historyTasks.removeFirst();
        }
    }
}