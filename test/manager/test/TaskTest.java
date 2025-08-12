package manager.test;

import manager.Status;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    @Test
    public void testEqualsById() {
        Task task1 = new Task(1, "subtask1", "desc1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Task task2 = new Task(1, "subtask2", "desc2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));

        Assertions.assertEquals(task1, task2, "Объекты с одной ID не равны друг к другу");
    }
}