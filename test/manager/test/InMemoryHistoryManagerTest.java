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
        Task task3 = new Task(1, "task3", "desc3", Status.NEW);

        historyManager.addTask(task1);
        manager.updateTask(task3);
        historyManager.addTask(manager.findTaskById(1));


        Assertions.assertEquals(historyManager.getHistory().getFirst(), historyManager.getHistory().get(1));


    }

}