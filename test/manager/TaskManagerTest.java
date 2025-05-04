/*
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class TaskManagerTest<T extends Manager.TaskManager> {
    protected T taskManager;
    protected File testFile;

    // Общие методы для всех реализаций Manager.TaskManager
    protected void setUp() throws Exception {
        // Инициализация будет в конкретных классах-наследниках
    }

    protected List<Data.Task> createTestTasks() {
        List<Data.Task> tasks = new LinkedList<>();

        Data.Task task1 = new Data.Task(
                "Test1",
                "Task1",
                Data.Status.NEW,
                Data.Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 1, 0)
        );

        Data.Task task2 = new Data.Task(
                "Test2",
                "Task2",
                Data.Status.NEW,
                Data.Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 1, 0)
        );

        Data.Epic epic1 = new Data.Epic("Test3", "Epic1", Data.Variety.EPIC);
        Data.Epic epic2 = new Data.Epic("Test4", "Epic2", Data.Variety.EPIC);

        taskManager.addEpic(epic1);

        Data.SubTask subTask1 = new Data.SubTask(
                "Test5",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2002, 1, 1, 1, 1, 0)
        );

        Data.SubTask subTask2 = new Data.SubTask(
                "Test6",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2003, 1, 1, 1, 1, 0)
        );

        Data.SubTask subTask3 = new Data.SubTask(
                "Test7",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Data.Status.NEW,
                Data.Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2004, 1, 1, 1, 1, 0)
        );


        tasks.add(task1);
        tasks.add(task2);
        tasks.add(epic1);
        tasks.add(epic2);
        tasks.add(subTask1);
        tasks.add(subTask2);
        tasks.add(subTask3);

        return tasks;
    }

    protected void addAllTasksToManager(List<Data.Task> tasks) {
        tasks.forEach(task -> {
            if (task.getVariety() == Data.Variety.TASK) {
                taskManager.addTask(task);
            } else if (task.getVariety() == Data.Variety.EPIC) {
                if (!task.getName().equals("Test3")) {
                    taskManager.addEpic((Data.Epic) task);
                }
            } else if (task.getVariety() == Data.Variety.SUBTASK) {
                if (task.getDescription().equals("In Data.Epic 1")) {
                    for (Data.Task task1 : taskManager.getAllEpics()) {
                        if (task1.getDescription().equals("Epic1")) {
                            ((Data.SubTask)task).setIdMasterTask(task1);
                        }
                    }
                }
                taskManager.addSubTask((Data.SubTask) task);
            }
        });
    }
}*/
