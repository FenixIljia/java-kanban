/*
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    FileBackedTaskManager fileBackedTaskManager;
    File file;
    File fileHistory;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("Test", "cvs");
            fileHistory = File.createTempFile("TestHistory", "cvs");
            fileBackedTaskManager = new FileBackedTaskManager(file.toPath(), fileHistory.toPath());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void setUp() throws Exception {
        testFile = File.createTempFile("tasks", ".csv");
        taskManager = FileBackedTaskManager.loadFromFile(testFile.toPath(), fileHistory.toPath());
    }

    @AfterEach
    public void after() {
        file.deleteOnExit();
    }

    @Test
    public void getNotNullAtSerialization() {
        Assertions.assertNotNull(fileBackedTaskManager);
        Assertions.assertTrue(Files.exists(file.toPath()));
        FileBackedTaskManager test = FileBackedTaskManager.loadFromFile(file.toPath(), fileHistory.toPath());
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


        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(testFile.toPath(), fileHistory.toPath());

        List<Task> actualTasks = new ArrayList<>();
        actualTasks.addAll(newManager.getAllTasks());
        actualTasks.addAll(newManager.getAllEpics());
        actualTasks.addAll(newManager.getAllSubTasks());

        Assertions.assertIterableEquals(filteredExpected, actualTasks, "Списки не должны быть разными");
    }}
*/
