package manager.test;

import manager.Epic;
import manager.Status;
import manager.Subtask;
import manager.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    public void testEqualsById(){
        Subtask subtask1 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1);
        Subtask subtask2 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1);

        Assertions.assertEquals(subtask1, subtask2, "Объекты с одной ID не равны друг к другу");
    }

    @Test
    public void testNotSeltAttaching() {
        Subtask subtask1 = new Subtask(1, "subtask1", "desc1", Status.NEW, 1);

        Assertions.assertEquals(subtask1.getId(), subtask1.getEpicId(), "Нельзя самого себя добавить в сабтаск");
    }

}