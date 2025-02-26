import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfTaskHaveSameId() {
        Task task = new Task("Test1", "Test1", Status.NEW, Variety.TASK);
        final int taskId = taskManager.addTask(task).getIdentifier();

        task.setIdentifier(taskId);

        final Task saveTask = taskManager.getTask(taskId);

        assertEquals(task, saveTask, "Задачи не равны.");
    }
}