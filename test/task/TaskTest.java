package task;

import data.Status;
import data.Task;
import data.Variety;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import exception.ManagerIntersectionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getTrueIfTaskHaveSameId() throws ManagerIntersectionException {
        Task task = new Task(
                "Test1",
                "Test1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        final int taskId = taskManager.addTask(task).getIdentifier();

        task.setIdentifier(taskId);

        final Task saveTask = taskManager.getTask(taskId);

        Assertions.assertEquals(task, saveTask, "Задачи не равны.");
    }
}