import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected File testFile;

    // Общие методы для всех реализаций TaskManager
    protected void setUp() throws Exception {
        // Инициализация будет в конкретных классах-наследниках
    }

    protected List<Task> createTestTasks() {
        List<Task> tasks = new LinkedList<>();

        Task task1 = new Task(
                "Test1",
                "Task1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );

        Task task2 = new Task(
                "Test2",
                "Task2",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 0, 0)
        );

        Epic epic1 = new Epic("Test3", "Epic1", Variety.EPIC);
        Epic epic2 = new Epic("Test4", "Epic2", Variety.EPIC);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask(
                "Test5",
                "In Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2002, 1, 1, 1, 0, 0)
        );

        SubTask subTask2 = new SubTask(
                "Test6",
                "In Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2003, 1, 1, 1, 0, 0)
        );

        SubTask subTask3 = new SubTask(
                "Test7",
                "In Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2004, 1, 1, 1, 0, 0)
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

    protected void addAllTasksToManager(List<Task> tasks) {
        tasks.forEach(task -> {
            if (task.getVariety() == Variety.TASK) {
                taskManager.addTask(task);
            } else if (task.getVariety() == Variety.EPIC) {
                taskManager.addEpic((Epic) task);
            } else if (task.getVariety() == Variety.SUBTASK) {
                if (task.getDescription().equals("In Epic 1")) {
                    for (Task task1 : taskManager.getAllEpics()) {
                        if (task1.getDescription().equals("Epic1")) {
                            ((SubTask)task).setIdMasterTask(task1);
                        }
                    }
                }
                taskManager.addSubTask((SubTask) task);
            }
        });
    }
}