import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> identifierConnectionSubTasks;

    public Epic(String name, String description, Variety variety) {
        super(name, description, variety);
        identifierConnectionSubTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, Variety variety) {
        super(name, description, status, variety);
        identifierConnectionSubTasks = new ArrayList<>();
    }

    public Task addSubTask(Task subTask) {
        if (Variety.SUBTASK.equals(subTask.getVariety())) {
            identifierConnectionSubTasks.add(subTask.getIdentifier());
            return subTask;
        }
        return null;
    }

    public ArrayList<Integer> getSubTask() {
        return identifierConnectionSubTasks;
    }

    public ArrayList<Integer> getIdentifierConnectionSubTasks() {
        return identifierConnectionSubTasks;
    }

    public void clearIdentifierConnectionSubTasks() {
        identifierConnectionSubTasks.clear();
    }

    public void removeSubTask(SubTask subTask) {
        identifierConnectionSubTasks.remove((Integer) subTask.getIdentifier());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic task = (Epic) o;
        return this.getIdentifier() == task.getIdentifier()
                && this.getName() == task.getName()
                && this.getDescription() == task.getDescription()
                && this.getStatus() == task.getStatus()
                && identifierConnectionSubTasks == task.getIdentifierConnectionSubTasks();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "identifierConnectionSubTasks=" + identifierConnectionSubTasks +
                '}';
    }
}
