package manager.test;

import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    File tempFile;
    FileBackedTaskManager backedTaskManager;

    @BeforeEach
    void createFile() throws IOException {
        tempFile = File.createTempFile("autosave", ".txt");
        backedTaskManager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void saveEmptyFileTest(){
        assertEquals(0, backedTaskManager.getTasks().size());
        assertEquals(0, backedTaskManager.getSubtasks().size());
        assertEquals(0, backedTaskManager.getEpics().size());
    }

    @Test
    void saveSomeTasksTest() {
        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Epic epic1 = new Epic("Epic1", "desc1");
        backedTaskManager.addNewTask(task1);
        final int idEpic1 = backedTaskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1", "desc1", Status.NEW, idEpic1);
        backedTaskManager.addNewSubtask(subtask1);
        assertEquals(1, backedTaskManager.getTasks().size());
        assertEquals(1, backedTaskManager.getSubtasks().size());
        assertEquals(1, backedTaskManager.getEpics().size());

    }

    @Test
    void shouldThrowManagerSaveException() {
        File file1 = new File("new_file");
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file1);
        }, "Файл с таким именем не найден");
    }
}