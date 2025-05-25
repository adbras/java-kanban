package manager.test;

import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    public void initTasks() {

        Task task1 = new Task(1, "task1", "desc1", Status.NEW);
        Task task2 = new Task(2, "task2", "desc2", Status.NEW);
        Task task3 = new Task(3, "task3", "desc3", Status.NEW);
        Task task4 = new Task(4, "task4", "desc4", Status.NEW);

        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.addTask(task4);
        historyManager.addTask(task3);
    }

    @Test
    public void shouldNotRemoveHistoryWhenAddTasks() {
        Assertions.assertEquals(historyManager.getHistory().size(), 4);
    }

    @Test
    public void checkLastTaskAfterAddTask() {
        Task lastTask = historyManager.getHistory().getLast();
        Assertions.assertEquals(3, lastTask.getId());
    }
}