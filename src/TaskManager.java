import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int identifier = 0;

    public void addTask(Task task) {
        identifier++;
        task.setIdentifier(identifier);
        tasks.put(identifier, task);
    }

    public void addEpic(Epic epic) {
        identifier++;
        epic.setIdentifier(identifier);
        epics.put(identifier, epic);
    }

    public void addSubTask(int id, SubTask subTask) {
        identifier++;
        subTask.setIdentifier(identifier);
        epics.get(id).addSubTask(subTask);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public HashMap<String, ArrayList<SubTask>> getAllSubTasks() {
        HashMap<String, ArrayList<SubTask>> allSubTasks = new HashMap<>();
        for (Epic epic : epics.values()) {
            allSubTasks.put(epic.getName(), epic.getSubTask());
        }

        return allSubTasks;
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpic() {
        epics.clear();
    }

    public void clearAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        for (Epic epic : epics.values()) {
            for (SubTask subTask : epic.getSubTask()) {
                if (subTask.getIdentifier() == id) {
                    return subTask;
                }
            }
        }

        return null;
    }

    public void upDateTask(int id, Task task) {
        task.setIdentifier(id);
        tasks.put(id, task);
    }

    public void upDateEpic(int id, Epic newEpic) {
        newEpic.setIdentifier(id);
        tasks.put(id, newEpic);
    }

    public void upDateSubTask(int id, SubTask newSubTask) {
        for (Epic epic : epics.values()) {
            for (SubTask subTask : epic.getSubTask()) {
                if (subTask.getIdentifier() == id) {
                    newSubTask.setIdentifier(id);
                    epic.addSubTask(newSubTask);
                }
            }
        }
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        epics.remove(id);
    }

    public void removeSubtask(int id) {
        for (Epic epic : epics.values()) {
            for (SubTask subTask : epic.getSubTask()) {
                if (subTask.getIdentifier() == id) {
                    epic.removeSubTaskInEpic(id);
                }
            }
        }
    }

    public ArrayList<SubTask> getSubTaskSpecificEpic(Integer id) {
        return epics.get(id).getSubTask();
    }
}
