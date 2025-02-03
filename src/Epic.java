import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> connectionSubTask;

    public Epic(String name, String description) {
        super(name, description);
        connectionSubTask = new HashMap<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        connectionSubTask = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {
        int i = 0;

        connectionSubTask.put(subTask.getIdentifier(), subTask);

        for (SubTask value : connectionSubTask.values()) {
            if (value.getStatus() == Status.IN_PROGRESS) {
                setStatus(Status.IN_PROGRESS);
            } else if (value.getStatus() == Status.DONE) {
                i++;
                if (i == connectionSubTask.size()) {
                    setStatus(Status.DONE);
                }
            }
        }
    }

    public ArrayList<SubTask> getSubTask() {
        return new ArrayList<>(connectionSubTask.values());
    }

    public void clearSubTasks() {
        connectionSubTask.clear();
    }

    public void removeSubTaskInEpic(int id) {
        connectionSubTask.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "connectionSubTask=" + connectionSubTask +
                '}';
    }
}
