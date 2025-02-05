import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> identifierConnectionSubTasks;

    public Epic(String name, String description) {
        super(name, description);
        identifierConnectionSubTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        identifierConnectionSubTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        identifierConnectionSubTasks.add(subTask.getIdentifier());
    }

    public ArrayList<Integer> getSubTask() {
        return identifierConnectionSubTasks;
    }

    public void clearIdentifierConnectionSubTasks() {
        identifierConnectionSubTasks.clear();
    }

    public void removeSubTask(SubTask subTask) {
        identifierConnectionSubTasks.remove((Integer) subTask.getIdentifier());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "identifierConnectionSubTasks=" + identifierConnectionSubTasks +
                '}';
    }
}
