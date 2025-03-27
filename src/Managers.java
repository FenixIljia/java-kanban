import java.nio.file.Path;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileBackedTaskManager(Path file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
