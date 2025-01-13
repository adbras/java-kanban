
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

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
        manager.printAllTasks();

        //2. получение задачи/подзадачи/эпика
        System.out.println("Все таски: \n" + manager.getTask());
        System.out.println("Все эпики: \n" + manager.getEpics());
        System.out.println("Все сабтаски: \n" + manager.getSubtask());
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
        manager.printAllTasks();
        Task task4 = new Task(1, "task1", "desc1.2", Status.DONE);
        manager.updateTask(task4);
        System.out.println("После обновления:");
        manager.printAllTasks();

        //2. обновление статуса подзадачи/эпика
        manager.updateSubtask(new Subtask(7, "subtask7", "desc7", Status.IN_PROGRESS, 4));
        manager.updateSubtask(new Subtask(8, "subtask8", "desc8", Status.IN_PROGRESS, 4));
        manager.updateSubtask(new Subtask(9, "subtask9", "desc9", Status.NEW, 6));

        //ВСЕ МЕТОДЫ УДАЛЕНИЯ
        //1. удаление
        manager.printAllTasks();
        manager.deleteTaskById(1); //удаление таска по Айди
        manager.printAllTasks(); //проверка вывода
        manager.deleteAllTasks(); //удаление всех тасков
        manager.printAllTasks(); //проверка вывода

        manager.deleteSubtaskById(7); //удаление сабтасков по Айди и обновление статуса эпика
        manager.printAllTasks();
        manager.deleteAllSubtasks(); //удаление всех сабтасков
        manager.printAllTasks();


        manager.deleteEpicById(4); //удаление эпика по айди
        manager.printAllTasks();


        manager.deleteAllEpics(); //удаление всех эпиков
        manager.printAllTasks();



    }


}