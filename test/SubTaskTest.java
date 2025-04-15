import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubTaskTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfSubTaskHaveSameId() {
        Epic epic = new Epic("Test1", "Test1", Variety.EPIC);
        SubTask subTask = new SubTask(
                "Test1",
                "Test1",
                taskManager.addEpic(epic).getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );
        final int taskId = taskManager.addSubTask(subTask).getIdentifier();

        subTask.setIdentifier(taskId);

        final Task saveTask = taskManager.getSubTask(taskId);

        Assertions.assertEquals(subTask, saveTask, "Задачи не равны.");
    }
}