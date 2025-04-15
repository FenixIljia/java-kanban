/*
public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Test1", "Task1", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Task2", Status.NEW, Variety.TASK);
        Epic epic1 = new Epic("Test3", "Epic1", Status.NEW, Variety.EPIC);
        Epic epic2 = new Epic("Test4", "Epic2", Status.NEW, Variety.EPIC);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Test5", "SubTask1 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask2 = new SubTask("Test6", "SubTask2 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask3 = new SubTask("Test7", "SubTask3 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        manager.getTask(task1.getIdentifier());
        manager.getSubTask(subTask2.getIdentifier());
        manager.getSubTask(subTask1.getIdentifier());
        manager.getEpic(epic1.getIdentifier());
        manager.getTask(task2.getIdentifier());
        manager.getEpic(epic2.getIdentifier());
        manager.getEpic(epic1.getIdentifier());
        manager.getTask(task1.getIdentifier());
        manager.getSubTask(subTask1.getIdentifier());
        manager.getSubTask(subTask3.getIdentifier());
        manager.getSubTask(subTask3.getIdentifier());


        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task.getDescription());

        }

        manager.removeEpic(epic1.getIdentifier());

        System.out.println("История: ");

        for (Task historyManager : manager.getHistory()) {
            System.out.println(historyManager.getDescription());
        }
    }
}
*/
