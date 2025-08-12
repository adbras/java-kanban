package manager.test;

import manager.Status;
import tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
    public void testEqualsById() {
        Subtask subtask1 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Subtask subtask2 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));

        Assertions.assertEquals(subtask1, subtask2, "Объекты с одной ID не равны друг к другу");
    }

    @Test
    public void testNotSeltAttaching() {
        Subtask subtask1 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));

        Assertions.assertEquals(subtask1.getId(), subtask1.getEpicId(), "Нельзя самого себя добавить в сабтаск");
    }

}