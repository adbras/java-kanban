import manager.*;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.IN_PROGRESS);
        Task task3 = new Task("Task3", "Desc3", Status.DONE);
        Epic epic1 = new Epic("Epic1", "desc1");
        Epic epic2 = new Epic("Epic2", "desc2");
        Epic epic3 = new Epic("Epic3", "desc3");
        final int idTask1 = manager.addNewTask(task1);
        final int idTask2 = manager.addNewTask(task2);
        final int idTask3 = manager.addNewTask(task3);
        final int idEpic1 = manager.addNewEpic(epic1);
        final int idEpic2 = manager.addNewEpic(epic2);
        final int idEpic3 = manager.addNewEpic(epic3);

        Subtask subtask1 = new Subtask("Subtask1", "desc1", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("Subtask2", "desc2", Status.NEW, idEpic1);
        Subtask subtask3 = new Subtask("Subtask3", "desc3", Status.DONE, idEpic3);
        final int idSubtask1 = manager.addNewSubtask(subtask1);
        final int idSubtask2 = manager.addNewSubtask(subtask2);
        final int idSubtask3 = manager.addNewSubtask(subtask3);


        //ВСЕ МЕТОДЫ ПОЛУЧЕНИЯ ДАННЫХ
        //1. вывод всех задач/подзадач/эпиков
        InMemoryTaskManager.printAllTasks(manager);

        //2. получение задачи/подзадачи/эпика
        System.out.println("Все таски: \n" + manager.getTasks());
        System.out.println("Все эпики: \n" + manager.getEpics());
        System.out.println("Все сабтаски: \n" + manager.getSubtasks());
        System.out.println("Поиск таска по ID: \n" + manager.findTaskById(3));
        System.out.println("Поиск таска по ID: \n" + manager.findTaskById(1));
        System.out.println("Поиск эпика по ID: \n" + manager.findEpicById(4));
        System.out.println("Поиск эпика по ID: \n" + manager.findEpicById(1));
        System.out.println("Поиск подзадачи по ID: \n" + manager.findSubtaskById(4));
        System.out.println("Поиск подзадачи по ID: \n" + manager.findSubtaskById(7));


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
        manager.deleteTaskById(1); //удаление таска по Айди
        InMemoryTaskManager.printAllTasks(manager);//проверка вывода
        manager.deleteAllTasks(); //удаление всех тасков
        InMemoryTaskManager.printAllTasks(manager);

        manager.deleteSubtaskById(7); //удаление сабтасков по Айди и обновление статуса эпика
        InMemoryTaskManager.printAllTasks(manager);
        manager.deleteAllSubtasks(); //удаление всех сабтасков
        InMemoryTaskManager.printAllTasks(manager);


        manager.deleteEpicById(4); //удаление эпика по айди
        InMemoryTaskManager.printAllTasks(manager);


        manager.deleteAllEpics(); //удаление всех эпиков
        InMemoryTaskManager.printAllTasks(manager);



    }


}