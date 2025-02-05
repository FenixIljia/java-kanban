import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int identifier = 0;

    public void addTask(Task task) {
        task.setIdentifier(generationIdentifier());
        tasks.put(task.getIdentifier(), task);
    }

    public void addEpic(Epic epic) {
        epic.setIdentifier(generationIdentifier());
        epics.put(epic.getIdentifier(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setIdentifier(generationIdentifier());
        subTasks.put(subTask.getIdentifier(), subTask);
        epics.get(subTask.getIdMasterTask()).addSubTask(subTask);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void clearAllSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.clearIdentifierConnectionSubTasks();
            upDataStatusEpic(epic);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void upDateTask(Task newTask) {
        tasks.put(newTask.getIdentifier(), newTask);
    }

    public void upDateEpic(Epic newEpic) {
        tasks.put(newEpic.getIdentifier(), newEpic);
    }

    public void upDateSubTask(SubTask newSubTask) {
        subTasks.put(newSubTask.getIdentifier(), newSubTask);

        upDataStatusEpic(epics.get(newSubTask.getIdMasterTask()));
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        for (Integer integer : epics.get(id).getSubTask()) {
           subTasks.remove(integer);
        }

        epics.remove(id);
    }

    public void removeSubtask(int id) {
        int identifierMasterEpic = subTasks.get(id).getIdMasterTask();

        epics.get(identifierMasterEpic).removeSubTask(subTasks.get(id));
        subTasks.remove(id);
        upDataStatusEpic(epics.get(identifierMasterEpic));
    }

    public ArrayList<SubTask> getSubTaskSpecificEpic(Integer id) {
        ArrayList<SubTask> subTasksSpecialEpic = new ArrayList<>();

        for (Integer integer : epics.get(id).getSubTask()) {
            subTasksSpecialEpic.add(subTasks.get(integer));
        }

        return subTasksSpecialEpic;
    }

    private void upDataStatusEpic(Epic epic) {
        int variableForCountingDone = 0;
        int variableForCountingNew = 0;

        for (Integer integer : epic.getSubTask()) {
            if (getSubTask(integer).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (getSubTask(integer).getStatus() == Status.DONE) {
                variableForCountingDone++;

                if (variableForCountingDone == epic.getSubTask().size()) {
                    epic.setStatus(Status.DONE);
                }
            } else {
                variableForCountingNew++;

                if (variableForCountingNew == epic.getSubTask().size()) {
                    epic.setStatus(Status.NEW);
                }
            }
        }
    }

    private int generationIdentifier() {
        return identifier++;
    }
}
