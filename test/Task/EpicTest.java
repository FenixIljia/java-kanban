package Task;

import Data.*;
import Manager.InMemoryTaskManager;
import Manager.TaskManager;
import Exception.ManagerIntersectionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfEpicHaveSameId() {
        Epic epic = new Epic("Test1", "Test1", Variety.EPIC);
        final int taskId = taskManager.addEpic(epic).getIdentifier();

        epic.setIdentifier(taskId);

        final Task saveTask = taskManager.getEpic(taskId);

        Assertions.assertEquals(epic, saveTask, "Задачи не равны.");
    }

    @Test
    public void getEpicCorrectStatusAtAllSubTaskNew() throws ManagerIntersectionException {
        Epic epic = new Epic("Test1", "Epic1", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                "Test2",
                "SubTask1",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
                );
        SubTask subTask1 = new SubTask(
                "Test3",
                "SubTask2",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
        );
        SubTask subTask2 = new SubTask(
                "Test4",
                "SubTask3",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );
        SubTask subTask3 = new SubTask(
                "Test5",
                "SubTask4",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(4,1,1,1,1,1)
                );

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void getEpicCorrectStatusAtAllSubTaskDone() throws ManagerIntersectionException {
        Epic epic = new Epic("Test1", "Epic1", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                "Test2",
                "SubTask1",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        SubTask subTask1 = new SubTask(
                "Test3",
                "SubTask2",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
                );
        SubTask subTask2 = new SubTask(
                "Test4",
                "SubTask3",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );
        SubTask subTask3 = new SubTask(
                "Test5",
                "SubTask4",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(4,1,1,1,1,1)
        );

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void getEpicCorrectStatusAtSubTasksNewAndDone() throws ManagerIntersectionException {

        Epic epic = new Epic("Test1", "Epic1", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                "Test2",
                "SubTask1",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        SubTask subTask1 = new SubTask(
                "Test3",
                "SubTask2",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1));
        SubTask subTask2 = new SubTask(
                "Test4",
                "SubTask3",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );
        SubTask subTask3 = new SubTask(
                "Test5",
                "SubTask4",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(4,1,1,1,1,1)
        );

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void getEpicCorrectStatusAtSubTasksIN_PROGRESS() throws ManagerIntersectionException {

        Epic epic = new Epic("Test1", "Epic1", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(
                "Test2",
                "SubTask1",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        SubTask subTask1 = new SubTask(
                "Test3",
                "SubTask2",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1));
        SubTask subTask2 = new SubTask(
                "Test4",
                "SubTask3",
                epic.getIdentifier(),
                Status.IN_PROGRESS,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(3,1,1,1,1,1)
        );
        SubTask subTask3 = new SubTask(
                "Test5",
                "SubTask4",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(4,1,1,1,1,1)
        );

        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}