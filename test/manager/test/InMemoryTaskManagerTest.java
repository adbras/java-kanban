package manager.test;

import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    TaskManager manager;
    private File tempFile;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void taskAddTask() {
        Task task1 = new Task("Task1", "Desc1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        manager.addNewTask(task1);
        Assertions.assertEquals(1, manager.getTasks().size(), "Таск не добавлен");
        Task task2 = manager.getTasks().get(0);
        Assertions.assertEquals(task1, task2, "Таск не добавлен");

    }

    @Test
    public void checkNotChangeAfterAddNewTask() {
        int id = 1;
        String name = "Task1";
        String desc = "dec1";
        Status status = Status.NEW;
        Duration duration = Duration.ofMinutes(10);
        LocalDateTime localDateTime = LocalDateTime.of(2025, 6, 11, 11, 00);
        Task task = new Task(id, name, desc, status, duration, localDateTime);
        manager.addNewTask(task);
        Task taskAfter = manager.getTasks().get(0);
        Assertions.assertEquals(taskAfter.getId(), id);
        Assertions.assertEquals(taskAfter.getStatus(), status);
        Assertions.assertEquals(taskAfter.getTaskName(), name);
        Assertions.assertEquals(taskAfter.getDescription(), desc);
    }

    @Test
    void shouldAddSubtaskToEpicAndLinkCorrectly() {
        Epic epic = new Epic("Epic1", "Epic desc", Duration.ZERO, LocalDateTime.now());
        manager.addNewEpic(epic);
        int epicId = epic.getId();

        Subtask sub = new Subtask("Subtask1", "Sub desc", Status.NEW, epicId, Duration.ofMinutes(30),
                LocalDateTime.of(2025, 8, 10, 10, 0));
        manager.addNewSubtask(sub);

        Epic updatedEpic = manager.findEpicById(epicId);
        Assertions.assertTrue(updatedEpic.getIdSubtaskEpics().contains(sub.getId()), "Subtask не прикреплён к эпику");
    }
}