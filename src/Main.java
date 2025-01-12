
public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Task1", "Desc1", Status.NEW);
        Task task2 = new Task("Task2", "Desc2", Status.IN_PROGRESS);
        Task task3 = new Task("Task3", "Desc3", Status.DONE);
        Epic epic1 = new Epic("Epic1", "desc1");
        Subtask subtask1 = new Subtask("Subtask1", "desc1", Status.NEW, epic1);
        Subtask subtask2 = new Subtask("Subtask2", "desc2", Status.NEW, epic1);

        //добавление задач/эпиков/подзадач
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.addNewTask(task3);


        //ВСЕ МЕТОДЫ ПОЛУЧЕНИЯ ДАННЫХ
        //1. вывод всех задач/подзадач/эпиков
        manager.printAllTasks();

        //2. получение задачи/подзадачи/эпика по ID
        manager.findTaskNameById(3);

        //3. получение всех подзадач определенного эпика
        manager.findSubtasksByEpic(epic1);


        //ВСЕ МЕТОДЫ ОБНОВЛЕНИЯ ДАННЫХ

        //1. обновление таска
        System.out.println("До обновления:");
        manager.printAllTasks();
        Task task4 = new Task(1, "task1", "desc1.2", Status.DONE);
        manager.updateTask(task4);
        System.out.println("После обновления:");
        manager.printAllTasks();

        //2. обновление статуса подзадачи/эпика
        manager.updateSubtask(new Subtask(4, "subtask1", "desc1", Status.DONE, epic1));
        manager.updateSubtask(new Subtask(5, "subtask2", "desc2", Status.DONE, epic1));


        //ВСЕ МЕТОДЫ УДАЛЕНИЯ
        //1. удаление по Id
        manager.printAllTasks();
        manager.deleteTaskById(1);
        manager.printAllTasks();

        //2. удаление всех задач/подзадач/эпиков
        manager.deleteAllTasks();


    }


}