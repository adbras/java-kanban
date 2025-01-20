package manager.test;
import manager.Managers;
import manager.Status;
import manager.Task;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.attribute.standard.MediaName;

class InMemoryTaskManagerTest {

    TaskManager manager;

    @BeforeEach
    public void initManager (){
        manager = Managers.getDefault();
    }

    @Test
    public void taskAddTask(){
        Task task1 = new Task(1, "task1", "desc1", Status.NEW);
        manager.addNewTask(task1);
        Assertions.assertEquals(1, manager.getTasks().size(), "Таск не добавлен");
        Task task2 = manager.getTasks().get(0);
        Assertions.assertEquals(task1, task2, "Таск не добавлен");

    }

    @Test
    public void checkNotChangeAfterAddNewTask(){
        int id=1;
        String name = "Task1";
        String desc = "dec1";
        Status status = Status.NEW;
        Task task = new Task(id, name, desc, status);
        manager.addNewTask(task);
        Task taskAfter = manager.getTasks().get(0);
        Assertions.assertEquals(taskAfter.getId(), id);
        Assertions.assertEquals(taskAfter.getStatus(), status);
        Assertions.assertEquals(taskAfter.getTaskName(), name);
        Assertions.assertEquals(taskAfter.getDescription(), desc);

    }
}