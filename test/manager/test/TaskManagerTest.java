package manager.test;
import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T backedTaskManager;
    private Task task = new Task("Task1", "Desc1", Status.NEW, Duration.ofMinutes(10),
            LocalDateTime.of(2025, 6, 11, 11, 00));
    private Epic epic = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
            LocalDateTime.of(2025, 6, 11, 11, 00));
    private Subtask subtask = new Subtask("Subtask1", "desc1", Status.NEW, epic.getId(),
            Duration.ofMinutes(10),
            LocalDateTime.of(2025, 6, 11, 11, 00));

    @Test
    public void testTaskEqualsById() {
        Task task1 = new Task(1, "subtask1", "desc1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Task task2 = new Task(1, "subtask2", "desc2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));

        Assertions.assertEquals(task1, task2, "Объекты с одной ID не равны друг к другу");
    }

    @Test
    public void testEpicEqualsById() {
        Epic epic1 = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Epic epic2 = new Epic(1, "epic2", "desc2", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Assertions.assertEquals(epic1, epic2, "Объекты с одной ID не равны друг к другу");
    }

    @Test
    public void testTaskIntervalOverlap() {
        LocalDateTime startTime1 = LocalDateTime.of(2025, 7, 11, 11, 00);
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW,
                 Duration.ofHours(1), startTime1);

        LocalDateTime startTime2 = LocalDateTime.of(2025, 6, 11, 11, 00);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW,
                 Duration.ofHours(1),startTime2);

        backedTaskManager.addNewTask(task1);
        backedTaskManager.addNewTask(task2);

        assertTrue(backedTaskManager.checkCrossTime(task1, task2), "Задачи должны перекрываться.");
    }

}
