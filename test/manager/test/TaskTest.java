package manager.test;

import manager.Status;
import manager.Subtask;
import manager.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void testEqualsById(){
        Task task1 = new Task(1, "subtask1", "desc1", Status.NEW);
        Task task2 = new Task(1, "subtask2", "desc2", Status.NEW);

        Assertions.assertEquals(task1, task2, "Объекты с одной ID не равны друг к другу");
    }
}