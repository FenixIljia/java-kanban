import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager;
    File file;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("Test", "cvs");
            fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @AfterEach
    public void after() {
        file.deleteOnExit();
    }

    @Test
    public void getNotNullAtSerialization() {
        Assertions.assertNotNull(fileBackedTaskManager);
        Assertions.assertTrue(fileBackedTaskManager.save());
    }

    @Test
    public void getNotNullAtUploadingFromFile() {

        Task task1 = new Task("Test1", "Task1", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Task2", Status.NEW, Variety.TASK);
        Epic epic1 = new Epic("Test3", "Epic1", Status.NEW, Variety.EPIC);
        Epic epic2 = new Epic("Test4", "Epic2", Status.NEW, Variety.EPIC);

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Test5", "SubTask1 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask2 = new SubTask("Test6", "SubTask2 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask3 = new SubTask("Test7", "SubTask3 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);

        fileBackedTaskManager.addSubTask(subTask1);
        fileBackedTaskManager.addSubTask(subTask2);
        fileBackedTaskManager.addSubTask(subTask3);

        ArrayList<Task> tasks = new ArrayList<>();

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(epic1);
        tasks.add(epic2);
        tasks.add(subTask1);
        tasks.add(subTask2);
        tasks.add(subTask3);

        ArrayList<Task> newTasks = new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);

            while (br.ready()) {
                String line = br.readLine();
                Task task = FileBackedTaskManager.fromString(line);

                if (task != null) {
                    newTasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        Assertions.assertEquals(tasks.toString(), newTasks.toString());
    }
}
