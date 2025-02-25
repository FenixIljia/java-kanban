import java.util.ArrayList;

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
        SubTask subTask3 = new SubTask("Test7", "SubTask3 in Epic 2", epic2.getIdentifier(), Status.NEW, Variety.SUBTASK);

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        
         System.out.println("Задачи:");
            for (Task task : manager.getAllTasks()) {
                System.out.println(task);
            }
            System.out.println("Эпики:");
            for (Epic epic : manager.getAllEpics()) {
                System.out.println(epic);

                ArrayList<SubTask> subTasksInEpic = manager.getSubTaskSpecificEpic(epic.getIdentifier());
                
                for (SubTask subTaskInEpic : subTasksInEpic) {
                    System.out.println("-->" + subTaskInEpic);
                }
            }
            System.out.println("Подзадачи:");
            for (Task subtask : manager.getAllSubTasks()) {
                System.out.println(subtask);
            }

            System.out.println("История:");
            for (Task task : manager.getHistory()) {
                System.out.println(task.getDescription());
            
        }
    }
}
