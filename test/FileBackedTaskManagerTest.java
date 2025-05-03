import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
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

    @Override
    protected void setUp() throws Exception {
        testFile = File.createTempFile("tasks", ".csv");
        taskManager = FileBackedTaskManager.loadFromFile(testFile.toPath());
    }

    @AfterEach
    public void after() {
        file.deleteOnExit();
    }

    @Test
    public void getNotNullAtSerialization() {
        Assertions.assertNotNull(fileBackedTaskManager);
        Assertions.assertTrue(Files.exists(file.toPath()));
        FileBackedTaskManager test = FileBackedTaskManager.loadFromFile(file.toPath());
        Assertions.assertNotNull(test);
    }

    @Test
    public void getNotNullAtUploadingFromFile() throws Exception {
        setUp();

        List<Task> expectedTasks = createTestTasks();

        addAllTasksToManager(expectedTasks);

        List<Task> filteredExpected = expectedTasks.stream()
                .filter(t -> t.getStartTime() != null)
                .toList();


        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(testFile.toPath());

        List<Task> actualTasks = new ArrayList<>();
        actualTasks.addAll(newManager.getAllTasks());
        actualTasks.addAll(newManager.getAllEpics());
        actualTasks.addAll(newManager.getAllSubTasks());

        Assertions.assertEquals(filteredExpected.toString(), actualTasks.toString());
    }}
