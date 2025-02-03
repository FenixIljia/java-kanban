public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Test1", "Task1");
        Task task2 = new Task("Test2", "Task2");
        Epic epic1 = new Epic("Test3", "Epic1");
        Epic epic2 = new Epic("Test4", "Epic2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Test5", "SubTask1 in Epic 1", epic1.getIdentifier());
        SubTask subTask2 = new SubTask("Test6", "SubTask2 in Epic 1", epic1.getIdentifier());
        SubTask subTask3 = new SubTask("Test7", "SubTask3 in Epic 2", epic2.getIdentifier());

        taskManager.addSubTask(3, subTask1);
        taskManager.addSubTask(3, subTask2);
        taskManager.addSubTask(4, subTask3);

        System.out.println();
        System.out.println("-".repeat(10));
        System.out.println();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        Task task4 = new Task("Test1", "UpData task", Status.IN_PROGRESS);
        Task task5 = new Task("Test2", "UpData Task", Status.DONE);

        task4.setIdentifier(task1.getIdentifier());
        task5.setIdentifier(task2.getIdentifier());

        taskManager.upDateTask(1, task4);
        taskManager.upDateTask(2, task5);

        SubTask subTask4 = new SubTask("Test8", "upData SubTask1 in Epic 1", subTask1.getIdMasterTask(), Status.IN_PROGRESS);
        SubTask subTask5 = new SubTask("Test9", "upData SubTask2 in Epic 1", subTask2.getIdMasterTask());
        SubTask subTask6 = new SubTask("Test10", "UpData SubTask3 in Epic 2", subTask3.getIdMasterTask(), Status.DONE);

        subTask4.setIdentifier(subTask1.getIdentifier());
        subTask5.setIdentifier(subTask2.getIdentifier());
        subTask6.setIdentifier(subTask3.getIdentifier());

        taskManager.upDateSubTask(subTask4.getIdentifier(), subTask4);
        taskManager.upDateSubTask(subTask5.getIdentifier(), subTask5);
        taskManager.upDateSubTask(subTask6.getIdentifier(), subTask6);

        System.out.print(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(3) + " - " + taskManager.getEpic(3).getStatus());
        System.out.println(taskManager.getEpic(4) + " - " + taskManager.getEpic(4).getStatus());
        System.out.println(taskManager.getSubTask(5) + " - " + taskManager.getSubTask(5).getStatus());
        System.out.println(taskManager.getSubTask(6) + " - " + taskManager.getSubTask(6).getStatus());
        System.out.println(taskManager.getSubTask(7) + " - " + taskManager.getSubTask(7).getStatus());

        taskManager.removeTask(1);
        taskManager.removeEpic(3);
    }
}
