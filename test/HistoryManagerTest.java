import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getNotNullAtGetHistoryTask() {
        Task task1 = new Task(
                "",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        Task task2 = new Task(
                "Test2",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
        );
        Task task3 = new Task(
                "Test3",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.getTask(task1.getIdentifier());
        taskManager.getTask(task2.getIdentifier());
        taskManager.getTask(task1.getIdentifier());
        taskManager.getTask(task3.getIdentifier());

        List<Task> history = new LinkedList<>();

        history.add(task2);
        history.add(task1);
        history.add(task3);

        Assertions.assertEquals(history, taskManager.getHistory(), "Менеджер должен хранить историю просмотров");
    }

    @Test
    public void getNotNullAtGetHistoryEpic() {
        Epic task1 = new Epic("", "", Variety.EPIC);
        Epic task2 = new Epic("", "", Variety.EPIC);
        Epic task3 = new Epic("", "", Variety.EPIC);

        taskManager.addEpic(task1);
        taskManager.addEpic(task2);
        taskManager.addEpic(task3);

        taskManager.getEpic(task1.getIdentifier());
        taskManager.getEpic(task2.getIdentifier());
        taskManager.getEpic(task1.getIdentifier());
        taskManager.getEpic(task3.getIdentifier());

        ArrayList<Task> history = new ArrayList<>();

        history.add(task2);
        history.add(task1);
        history.add(task3);

        Assertions.assertEquals(history, taskManager.getHistory(), "Менеджер должен хранить историю просмотров");
    }

    @Test
    public void getNotNullAtGetHistorySubTask() {
        Epic epic = new Epic("", "", Variety.EPIC);

        int idEpic = taskManager.addEpic(epic).getIdentifier();
        SubTask task1 = new SubTask(
                "",
                "", idEpic,
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );
        SubTask task2 = new SubTask(
                "",
                "",
                idEpic,
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 0, 0)
        );
        SubTask task3 = new SubTask(
                "",
                "",
                idEpic,
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2002, 1, 1, 1, 0, 0)
        );

        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        taskManager.addSubTask(task3);

        taskManager.getSubTask(task1.getIdentifier());
        taskManager.getSubTask(task2.getIdentifier());
        taskManager.getSubTask(task1.getIdentifier());
        taskManager.getSubTask(task3.getIdentifier());

        ArrayList<Task> history = new ArrayList<>();

        history.add(task2);
        history.add(task1);
        history.add(task3);

        Assertions.assertEquals(history, taskManager.getHistory(), "Менеджер должен хранить историю просмотров");
    }

    @Test
    public void getWorksCorrectlyHistoryList() {
        Task task1 = new Task(
                "",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        Task task2 = new Task(
                "Test2",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
        );
        Task task3 = new Task(
                "Test3",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.getTask(task1.getIdentifier());
        taskManager.getTask(task2.getIdentifier());
        taskManager.getTask(task1.getIdentifier());
        taskManager.getTask(task3.getIdentifier());

        Assertions.assertEquals(taskManager.getHistory().get(1), task1);
        Assertions.assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    public void getEmptyHistoryLis() {
        Assertions.assertNull(taskManager.getHistory());
    }

    @Test
    public void getCorrectAtRemoveFromHistory() {
        Task task1 = new Task(
                "Test1",
                "Test1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1, 1, 1, 1, 1, 1)
        );
        Task task2 = new Task(
                "Test2",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2, 1, 1, 1, 1, 1)
        );
        Task task3 = new Task(
                "Test3",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3, 1, 1, 1, 1, 1)
        );
        Task task4 = new Task(
                "Test3",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(4, 1, 1, 1, 1, 1)
        );
        Task task5 = new Task(
                "Test4",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(5, 1, 1, 1, 1, 1)
        );
        Task task6 = new Task(
                "Test5",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(6, 1, 1, 1, 1, 1)
        );
        Task task7 = new Task(
                "Test6",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(7, 1, 1, 1, 1, 1)
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        taskManager.addTask(task6);
        taskManager.addTask(task7);

        taskManager.getTask(task1.getIdentifier());
        taskManager.getTask(task2.getIdentifier());
        taskManager.getTask(task3.getIdentifier());
        taskManager.getTask(task4.getIdentifier());
        taskManager.getTask(task5.getIdentifier());
        taskManager.getTask(task6.getIdentifier());
        taskManager.getTask(task7.getIdentifier());

        Assertions.assertEquals(7, taskManager.getHistory().size());

        taskManager.removeTask(task1.getIdentifier());

        Assertions.assertEquals(taskManager.getHistory().get(0), task2);

        taskManager.removeTask(task4.getIdentifier());

        Assertions.assertEquals(taskManager.getHistory().get(2), task5);

        taskManager.removeTask(task7.getIdentifier());

        Assertions.assertEquals(taskManager.getHistory().get(3), task6);
    }
}
