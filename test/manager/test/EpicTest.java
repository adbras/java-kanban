package manager.test;
import org.junit.jupiter.api.Assertions;

import manager.Epic;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    public void testEqualsById() {
        Epic epic1 = new Epic(1, "epic1", "desc1");
        Epic epic2 = new Epic(1, "epic2", "desc2");
        Assertions.assertEquals(epic1, epic2, "Объекты с одной ID не равны друг к другу");
    }

    @Test
    public void testShouldUniqSubtasksId() {
        Epic epic1 = new Epic(0, "epic1", "desc1");
        epic1.addSubtaskIdToEpics(1);
        epic1.addSubtaskIdToEpics(2);
        Assertions.assertEquals(2, epic1.getIdSubtaskEpics().size(), "В Эпике есть неуникальные Subtasks");
        epic1.addSubtaskIdToEpics(1);
        Assertions.assertEquals(2, epic1.getIdSubtaskEpics().size(), "В Эпике есть неуникальные Subtasks");
    }

    @Test
    public void testNotSeltAttaching() {
        Epic epic1 = new Epic(1, "epic1", "desc1");
        epic1.addSubtaskIdToEpics(1);

        Assertions.assertEquals(0, epic1.getIdSubtaskEpics().size(), "Эпик нельзя добавить в Сабтаск");
    }
}