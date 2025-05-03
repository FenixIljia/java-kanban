package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedTaskManager(Path file, Path fileHistory) {
        return new FileBackedTaskManager(file, fileHistory);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
