import manager.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        File tempFile = null;
        FileBackedTaskManager backedTaskManager = null;

        try {
            tempFile = File.createTempFile("autosave", ".txt");
            if (tempFile.length() == 0) {
                System.out.println("Файл создан и пуст — создаём новый менеджер.");
                backedTaskManager = new FileBackedTaskManager(tempFile);
            } else {
                System.out.println("Файл уже существует и содержит данные — загружаем менеджер из файла.");
                backedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
            }
        } catch (IOException e){

            System.out.println("Ошибка при создании временного файла" + e.getMessage());
        }

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.IN_PROGRESS);
        Task task3 = new Task("Task3", "Desc3", Status.DONE);
        Epic epic1 = new Epic("Epic1", "desc1");
        Epic epic2 = new Epic("Epic2", "desc2");
        Epic epic3 = new Epic("Epic3", "desc3");


        backedTaskManager.addNewTask(task1);
        backedTaskManager.addNewTask(task2);
        backedTaskManager.addNewTask(task3);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);
        final int idEpic1 = backedTaskManager.addNewEpic(epic1);
        final int idEpic3 = backedTaskManager.addNewEpic(epic3);
        backedTaskManager.addNewEpic(epic2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewEpic(epic3);

        Subtask subtask1 = new Subtask("Subtask1", "desc1", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("Subtask2", "desc2", Status.NEW, idEpic1);
        Subtask subtask3 = new Subtask("Subtask3", "desc3", Status.DONE, idEpic3);
        backedTaskManager.addNewSubtask(subtask1);
        backedTaskManager.addNewSubtask(subtask2);
        backedTaskManager.addNewSubtask(subtask3);

        //ВСЕ МЕТОДЫ ПОЛУЧЕНИЯ ДАННЫХ
        //1. вывод всех задач/подзадач/эпиков
        InMemoryTaskManager.printAllTasks(manager);

        //2. получение задачи/подзадачи/эпика
        System.out.println("Все таски: \n" + manager.getTasks());
        System.out.println("Все эпики: \n" + manager.getEpics());
        System.out.println("Все сабтаски: \n" + manager.getSubtasks());
        System.out.println("Поиск таска по ID: \n" + manager.findTaskById(3));
        System.out.println("Поиск таска по ID: \n" + manager.findTaskById(1));


        //3. получение всех подзадач определенного эпика
        System.out.println("Список задач эпика:" + manager.findSubtasksByEpic(epic1));


        //ВСЕ МЕТОДЫ ОБНОВЛЕНИЯ ДАННЫХ

        //1. обновление таска
        System.out.println("До обновления:");
        InMemoryTaskManager.printAllTasks(manager);
        Task task4 = new Task(1, "task1", "desc1.2", Status.DONE);
        manager.updateTask(task4);
        System.out.println("После обновления:");
        InMemoryTaskManager.printAllTasks(manager);

        //2. обновление статуса подзадачи/эпика
        manager.updateSubtask(new Subtask(7, "subtask7", "desc7", Status.IN_PROGRESS, 4));
        manager.updateSubtask(new Subtask(8, "subtask8", "desc8", Status.IN_PROGRESS, 4));
        manager.updateSubtask(new Subtask(9, "subtask9", "desc9", Status.NEW, 6));

        //ВСЕ МЕТОДЫ УДАЛЕНИЯ
        //1. удаление
        InMemoryTaskManager.printAllTasks(manager);
        backedTaskManager.deleteTaskById(1); //удаление таска по Айди
        InMemoryTaskManager.printAllTasks(manager);//проверка вывода
        manager.deleteAllTasks(); //удаление всех тасков
        InMemoryTaskManager.printAllTasks(manager);

        backedTaskManager.deleteSubtaskById(7); //удаление сабтасков по Айди и обновление статуса эпика
        InMemoryTaskManager.printAllTasks(manager);
        manager.deleteAllSubtasks(); //удаление всех сабтасков
        InMemoryTaskManager.printAllTasks(manager);


        backedTaskManager.deleteEpicById(4); //удаление эпика по айди
        InMemoryTaskManager.printAllTasks(manager);


        manager.deleteAllEpics(); //удаление всех эпиков
        InMemoryTaskManager.printAllTasks(manager);



    }


}