import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfSubTaskHaveSameId() {
        Epic epic = new Epic("Test1", "Test1", Status.NEW, Variety.EPIC);
        SubTask subTask = new SubTask("Test1", "Test1", taskManager.addEpic(epic).getIdentifier(), Status.NEW, Variety.SUBTASK);
        final int taskId = taskManager.addSubTask(subTask).getIdentifier();

        subTask.setIdentifier(taskId);

        final Task saveTask = taskManager.getSubTask(taskId);

        Assertions.assertEquals(subTask, saveTask, "Задачи не равны.");
    }
}