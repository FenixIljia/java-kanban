package manager;

import data.*;
import exception.ManagerIntersectionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getNullAddEpicInEpic() {
        Epic epic = new Epic("Test2", "Test2", Variety.EPIC);

        Assertions.assertNull(epic.addSubTask(epic), "Эпик нельзя добавить в качестве подзадачи.");
    }

    @Test
    public void getNullAddSubTaskInSubTask() {
        SubTask subTask = new SubTask(
                "Test3",
                "Test3",
                1,
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(20),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );

        Assertions.assertNull(subTask.setIdMasterTask(subTask), "Подзадачу нельзя сделать своим же эпиком");
    }

    @Test
    public void getNotNullAddTask() throws ManagerIntersectionException {
        Assertions.assertNotNull(taskManager.addTask(new Task(
                "Test4",
                "Test4",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAddEpic() {
        Assertions.assertNotNull(taskManager.addEpic(new Epic(
                "Test5",
                "Test5",
                Variety.EPIC
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAddSubTask() throws ManagerIntersectionException {
        Epic epic = new Epic("Data.Epic", "Data.Epic", Variety.EPIC);

        Assertions.assertNotNull(taskManager.addSubTask(new SubTask(
                "Test6",
                "Test6",
                taskManager.addEpic(epic).getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(20),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAtSearchTaskByIdentifier() throws ManagerIntersectionException {
        final int idTask = taskManager.addTask(new Task(
                "Test7",
                "Test7",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getTask(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtSearchEpicByIdentifier() {
        final int idTask = taskManager.addEpic(new Epic(
                "Test8",
                "Test8",
                Variety.EPIC
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getEpic(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtSearchSubTaskByIdentifier() throws ManagerIntersectionException {
        Epic epic = new Epic("Data.Epic", "Data.Epic", Variety.EPIC);

        final int idTask
                = taskManager.addSubTask(new SubTask(
                "Test9",
                "Test9",
                taskManager.addEpic(epic).getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getSubTask(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtEqualsTask1WithGenerationIdentifierWithTask2WithAddIdentifier() throws ManagerIntersectionException {
        Task task1 = new Task(
                "Test1",
                "Test1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        Task task2 = new Task(
                "Test2",
                "Test2",
                Status.DONE,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
        );

        task2.setIdentifier(1);

        int idTask1 = taskManager.addTask(task1).getIdentifier();
        int idTask2 = taskManager.addTask(task2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getTask(idTask1), taskManager.getTask(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullAtEqualsEpic1WithGenerationIdentifierWithEpic2WithAddIdentifier() {
        Epic epic1 = new Epic("Test1", "Test1", Variety.EPIC);
        Epic epic2 = new Epic("Test2", "Test2", Variety.EPIC);

        epic2.setIdentifier(1);

        int idTask1 = taskManager.addEpic(epic1).getIdentifier();
        int idTask2 = taskManager.addEpic(epic2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getEpic(idTask1), taskManager.getEpic(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullAtEqualsSubTask1WithGenerationIdentifierWithSubTask2WithAddIdentifier() throws ManagerIntersectionException {
        Epic epic = new Epic("", "", Variety.EPIC);
        taskManager.addEpic(epic);

        SubTask task1 = new SubTask("Test1",
                "Test1",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );
        SubTask task2 = new SubTask("Test2",
                "Test2",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 0, 0)
        );

        task2.setIdentifier(1);

        int idTask1 = taskManager.addSubTask(task1).getIdentifier();
        int idTask2 = taskManager.addSubTask(task2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getSubTask(idTask1), taskManager.getSubTask(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullWhenTaskNotChangesAtAddTaskManager() throws ManagerIntersectionException {
        Task task = new Task(
                "",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );
        int idTask = taskManager.addTask(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getTask(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullWhenEpicNotChangesAtAddEpicManager() {
        Epic task = new Epic("", "", Variety.EPIC);
        int idTask = taskManager.addEpic(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getEpic(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullWhenSubTaskNotChangesAtAddSubTaskManager() throws ManagerIntersectionException {
        Epic epic = new Epic("", "", Variety.EPIC);
        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "",
                "",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );
        int idTask = taskManager.addSubTask(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getSubTask(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullEqualsAfterUpDataTask() throws ManagerIntersectionException {
        Task task1 = new Task(
                "",
                "",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1,1,1,1,1,1)
        );

        taskManager.addTask(task1);

        Task task2 = new Task(
                "Test2",
                "Test2",
                Status.DONE,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(2,1,1,1,1,1)
        );

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateTask(task2);

        Assertions.assertEquals(task2, taskManager.getTask(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getNotNullEqualsAfterUpDataEpic() {
        Epic task1 = new Epic("", "", Variety.EPIC);

        taskManager.addEpic(task1);

        Epic task2 = new Epic("Test2", "Test2", Variety.EPIC);

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateEpic(task2);

        Assertions.assertEquals(task2, taskManager.getEpic(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getNotNullEqualsAfterUpDataSubTask() throws ManagerIntersectionException {
        Epic epic = new Epic("", "", Variety.EPIC);
        taskManager.addEpic(epic);
        SubTask task1 = new SubTask(
                "",
                "",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );

        taskManager.addSubTask(task1);

        SubTask task2 = new SubTask(
                "Test2",
                "Test2",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 0, 0)
        );

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateSubTask(task2);

        Assertions.assertEquals(task2, taskManager.getSubTask(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getWorksCorrectlyHistoryList() throws ManagerIntersectionException {
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
    }

    @Test
    public void getNullIdMasterEpicAfterRemoveSubTask() throws ManagerIntersectionException {
        Epic epic = new Epic("", "", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task1 = new SubTask("",
                "",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0)
        );

        taskManager.addSubTask(task1);

        taskManager.removeSubtask(task1.getIdentifier());
        Integer i = null;
        Assertions.assertEquals(i, task1.getIdMasterTask());
    }

    @Test
    public void getNullAtRemoveSubTask() throws ManagerIntersectionException {
        Epic epic = new Epic("", "", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("",
                "", epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 4, 6, 4, 50, 0)
        );
        SubTask subTask2 = new SubTask(
                "Test1",
                "Test1",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 0, 0)
        );
        SubTask subTask3 = new SubTask(
                "Test2",
                "Test2",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2002, 1, 1, 1, 0, 0)
        );
        SubTask subTask4 = new SubTask(
                "Test3",
                "Test3",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2003, 1, 1, 1, 0, 0)
        );
        SubTask subTask5 = new SubTask(
                "Test4",
                "Test4",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2004, 1, 1, 1, 0, 0)
        );
        SubTask subTask6 = new SubTask("Test5",
                "Test5",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2005, 1, 1, 1, 0, 0)
        );
        SubTask subTask7 = new SubTask(
                "Test6",
                "Test6",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2006, 1, 1, 1, 0, 0)
        );

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
        taskManager.addSubTask(subTask5);
        taskManager.addSubTask(subTask6);
        taskManager.addSubTask(subTask7);

        Assertions.assertEquals(7, epic.getSubTask().size());

        taskManager.removeSubtask(subTask1.getIdentifier());
        taskManager.removeSubtask(subTask7.getIdentifier());
        taskManager.removeSubtask(subTask5.getIdentifier());
        taskManager.removeSubtask(subTask3.getIdentifier());

        Assertions.assertEquals(3, epic.getSubTask().size());
    }

    @Test
    public void getCorrectIntersectionIntervals() throws ManagerIntersectionException {
        Task task = new Task(
                "Test1",
                "Test1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1, 1, 1, 1, 0, 0)
        );

        Task task1 = new Task(
                "Test2",
                "Test2",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(100),
                LocalDateTime.of(1, 1, 1, 2, 0, 0)
        );

        taskManager.addTask(task);

        Assertions.assertThrows(ManagerIntersectionException.class, () -> {
            taskManager.addTask(task1);
        }, "Дошлжно выбрасываться исключение при пересечении интервалов");
    }
}