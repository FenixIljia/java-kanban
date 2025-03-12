import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfEpicHaveSameId() {
        Epic epic = new Epic("Test1", "Test1", Status.NEW, Variety.EPIC);
        final int taskId = taskManager.addEpic(epic).getIdentifier();

        epic.setIdentifier(taskId);

        final Task saveTask = taskManager.getEpic(taskId);

        assertEquals(epic, saveTask, "Задачи не равны.");
    }
}