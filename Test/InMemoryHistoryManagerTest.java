import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getNullAddEpicInEpic() {
        Epic epic = new Epic("Test2", "Test2", Status.NEW, Variety.EPIC);

        Assertions.assertNull(epic.addSubTask(epic), "Эпик нельзя добавить в качестве подзадачи.");
    }

    @Test
    public void getNullAddSubTaskInSubTask() {
        SubTask subTask = new SubTask(
                "Test3",
                "Test3",
                1,
                Status.NEW,
                Variety.SUBTASK
        );

        Assertions.assertNull(subTask.setIdMasterTask(subTask), "Подзадачу нельзя сделать своим же эпиком");
    }

    @Test
    public void getNotNullAddTask() {
        Assertions.assertNotNull(taskManager.addTask(new Task(
                "Test4",
                "Test4",
                Status.NEW,
                Variety.TASK
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAddEpic() {
        Assertions.assertNotNull(taskManager.addEpic(new Epic(
                "Test5",
                "Test5",
                Status.NEW,
                Variety.EPIC
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAddSubTask() {
        Epic epic = new Epic("Epic", "Epic", Status.NEW, Variety.EPIC);

        Assertions.assertNotNull(taskManager.addSubTask(new SubTask(
                "Test6",
                "Test6",
                taskManager.addEpic(epic).getIdentifier(),
                Status.NEW,
                Variety.SUBTASK
        )), "Менеджер должен добавлять задачи.");
    }

    @Test
    public void getNotNullAtSearchTaskByIdentifier() {
        final int idTask = taskManager.addTask(new Task(
                "Test7",
                "Test7",
                Status.NEW,
                Variety.TASK
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getTask(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtSearchEpicByIdentifier() {
        final int idTask = taskManager.addEpic(new Epic(
                "Test8",
                "Test8",
                Status.NEW,
                Variety.EPIC
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getEpic(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtSearchSubTaskByIdentifier() {
        Epic epic = new Epic("Epic", "Epic", Status.NEW, Variety.EPIC);

        final int idTask
                = taskManager.addSubTask(new SubTask(
                "Test9",
                "Test9",
                taskManager.addEpic(epic).getIdentifier(),
                Status.NEW,
                Variety.SUBTASK
        )).getIdentifier();

        Assertions.assertNotNull(taskManager.getSubTask(idTask), "Менеджер должен находить задачу по ее identifier");
    }

    @Test
    public void getNotNullAtEqualsTask1WithGenerationIdentifierWithTask2WithAddIdentifier() {
        Task task1 = new Task("Test1", "Test1", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Test2", Status.DONE, Variety.TASK);

        task2.setIdentifier(1);

        int idTask1 = taskManager.addTask(task1).getIdentifier();
        int idTask2 = taskManager.addTask(task2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getTask(idTask1), taskManager.getTask(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullAtEqualsEpic1WithGenerationIdentifierWithEpic2WithAddIdentifier() {
        Epic epic1 = new Epic("Test1", "Test1", Status.NEW, Variety.EPIC);
        Epic epic2 = new Epic("Test2", "Test2", Status.DONE, Variety.EPIC);

        epic2.setIdentifier(1);

        int idTask1 = taskManager.addEpic(epic1).getIdentifier();
        int idTask2 = taskManager.addEpic(epic2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getEpic(idTask1), taskManager.getEpic(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullAtEqualsSubTask1WithGenerationIdentifierWithSubTask2WithAddIdentifier() {
        Epic epic = new Epic("Test", "Test", Status.NEW, Variety.EPIC);
        taskManager.addEpic(epic);

        SubTask task1 = new SubTask("Test1", "Test1", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask task2 = new SubTask("Test2", "Test2", epic.getIdentifier(), Status.DONE, Variety.SUBTASK);

        task2.setIdentifier(1);

        int idTask1 = taskManager.addSubTask(task1).getIdentifier();
        int idTask2 = taskManager.addSubTask(task2).getIdentifier();

        Assertions.assertNotEquals(taskManager.getSubTask(idTask1), taskManager.getSubTask(idTask2), "Должны вернуться разные задачи");
    }

    @Test
    public void getNotNullWhenTaskNotChangesAtAddTaskManager() {
        Task task = new Task("Test", "Test", Status.NEW, Variety.TASK);
        int idTask = taskManager.addTask(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getTask(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullWhenEpicNotChangesAtAddEpicManager() {
        Epic task = new Epic("Test", "Test", Status.NEW, Variety.EPIC);
        int idTask = taskManager.addEpic(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getEpic(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullWhenSubTaskNotChangesAtAddSubTaskManager() {
        Epic epic = new Epic("Test", "Test", Status.NEW, Variety.EPIC);
        taskManager.addEpic(epic);

        SubTask task = new SubTask("Test", "Test", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        int idTask = taskManager.addSubTask(task).getIdentifier();
        task.setIdentifier(idTask);

        Assertions.assertEquals(task, taskManager.getSubTask(idTask), "Задача должна остаться не изменной");
    }

    @Test
    public void getNotNullAtGetHistoryTask() {
        Task task1 = new Task("Test", "Test", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Test", Status.NEW, Variety.TASK);
        Task task3 = new Task("Test3", "Test", Status.NEW, Variety.TASK);

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
        Epic task1 = new Epic("Test", "Test", Status.NEW, Variety.EPIC);
        Epic task2 = new Epic("Test", "Test", Status.NEW, Variety.EPIC);
        Epic task3 = new Epic("Test", "Test", Status.NEW, Variety.EPIC);

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
        Epic epic = new Epic("Test", "Test", Status.NEW, Variety.EPIC);

        int idEpic = taskManager.addEpic(epic).getIdentifier();
        SubTask task1 = new SubTask("Test", "Test", idEpic, Status.NEW, Variety.SUBTASK);
        SubTask task2 = new SubTask("Test", "Test", idEpic, Status.NEW, Variety.SUBTASK);
        SubTask task3 = new SubTask("Test", "Test", idEpic, Status.NEW, Variety.SUBTASK);

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
    public void getNotNullEqualsAfterUpDataTask() {
        Task task1 = new Task("Test", "Test", Status.NEW, Variety.TASK);

        taskManager.addTask(task1);

        Task task2 = new Task("Test2", "Test2", Status.DONE, Variety.TASK);

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateTask(task2);

        Assertions.assertEquals(task2, taskManager.getTask(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getNotNullEqualsAfterUpDataEpic() {
        Epic task1 = new Epic("Test", "Test", Status.NEW, Variety.EPIC);

        taskManager.addEpic(task1);

        Epic task2 = new Epic("Test2", "Test2", Status.DONE, Variety.EPIC);

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateEpic(task2);

        Assertions.assertEquals(task2, taskManager.getEpic(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getNotNullEqualsAfterUpDataSubTask() {
        Epic epic = new Epic("Test", "Test", Status.DONE, Variety.EPIC);
        taskManager.addEpic(epic);
        SubTask task1 = new SubTask("Test", "Test", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);

        taskManager.addSubTask(task1);

        SubTask task2 = new SubTask("Test2", "Test2", epic.getIdentifier(), Status.DONE, Variety.SUBTASK);

        task2.setIdentifier(task1.getIdentifier());
        taskManager.upDateSubTask(task2);

        Assertions.assertEquals(task2, taskManager.getSubTask(task2.getIdentifier()), "Задача должна обновиться");
    }

    @Test
    public void getWorksCorrectlyHistoryList() {
        Task task1 = new Task("Test", "Test", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Test", Status.NEW, Variety.TASK);
        Task task3 = new Task("Test3", "Test", Status.NEW, Variety.TASK);

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
    public void getNullIdMasterEpicAfterRemoveSubTask() {
        Epic epic = new Epic("Test", "Test", Status.NEW, Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task1 = new SubTask("Test", "Test", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);

        taskManager.addSubTask(task1);

        taskManager.removeSubtask(task1.getIdentifier());
        Integer i = null;
        Assertions.assertEquals(i, task1.getIdMasterTask());
    }

    @Test
    public void getNullAtRemoveSubTask() {
        Epic epic = new Epic("Test", "Test", Status.NEW, Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Test", "Test", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask2 = new SubTask("Test1", "Test1", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask3 = new SubTask("Test2", "Test2", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask4 = new SubTask("Test3", "Test3", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask5 = new SubTask("Test4", "Test4", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask6 = new SubTask("Test5", "Test5", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask7 = new SubTask("Test6", "Test6", epic.getIdentifier(), Status.NEW, Variety.SUBTASK);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
        taskManager.addSubTask(subTask5);
        taskManager.addSubTask(subTask6);
        taskManager.addSubTask(subTask7);

        Assertions.assertEquals(7, epic.getIdentifierConnectionSubTasks().size());

        taskManager.removeSubtask(subTask1.getIdentifier());
        taskManager.removeSubtask(subTask7.getIdentifier());
        taskManager.removeSubtask(subTask5.getIdentifier());
        taskManager.removeSubtask(subTask3.getIdentifier());

        Assertions.assertEquals(3, epic.getIdentifierConnectionSubTasks().size());
    }
}