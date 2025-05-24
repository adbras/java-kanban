package manager.test;

import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {


    @Test
    public void shouldNotUpdateTasksWhenAddHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task(1, "task1", "desc1", Status.NEW);
        Task task3 = new Task(2, "task3", "desc3", Status.NEW);

        historyManager.addTask(task1);
        manager.updateTask(task3);
        System.out.println(historyManager.getHistory().getFirst());
        System.out.println(historyManager.getHistory().get(1));
        Assertions.assertEquals(historyManager.getHistory().getFirst(), historyManager.getHistory().get(1));
    }


    @Test
    public void shouldNotRemoveHistoryWhenAddTasks() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task(1, "task1", "desc1", Status.NEW);
        Task task2 = new Task(2, "task2", "desc2", Status.NEW);

        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task1);
        Assertions.assertEquals(historyManager.getHistory().size(), 2);
    }

    @Test
    public void checkLastTaskAfterAddTask() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task(1, "task1", "desc1", Status.NEW);
        Task task2 = new Task(2, "task2", "desc2", Status.NEW);
        Task task3 = new Task(3, "task3", "desc3", Status.NEW);
        Task task4 = new Task(4, "task4", "desc4", Status.NEW);

        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.addTask(task4);
        historyManager.addTask(task3);
        Assertions.assertEquals(historyManager.getHistory().getLast(), task3);
    }
}