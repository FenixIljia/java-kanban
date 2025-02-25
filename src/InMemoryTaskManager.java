import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int identifier = 0;
    private final HistoryManager historyManager = new InMemoryHistoryManager();


    @Override
    public Task addTask(Task task) {
        if (Variety.TASK.equals(task.getVariety())) {
            task.setIdentifier(generationIdentifier());
            tasks.put(task.getIdentifier(), task);
            return task;
        }
        return null;
    }

    @Override
    public Task addEpic(Epic epic) {
        if (Variety.EPIC.equals(epic.getVariety())) {
            epic.setIdentifier(generationIdentifier());
            epics.put(epic.getIdentifier(), epic);
            return epic;
        }
        return null;
    }

    @Override
    public Task addSubTask(SubTask subTask) {
        if (Variety.SUBTASK.equals(subTask.getVariety())) {
            subTask.setIdentifier(generationIdentifier());
            subTasks.put(subTask.getIdentifier(), subTask);
            epics.get(subTask.getIdMasterTask()).addSubTask(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        upDataHistory(epics.values());
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        upDataHistory(tasks.values());
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        upDataHistory(subTasks.values());
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.clearIdentifierConnectionSubTasks();
            upDataStatusEpic(epic);
        }
    }

    @Override
    public Task getTask(int id) {
        upDataHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        upDataHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        upDataHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void upDateTask(Task newTask) {
        tasks.put(newTask.getIdentifier(), newTask);
    }

    @Override
    public void upDateEpic(Epic newEpic) {
        epics.put(newEpic.getIdentifier(), newEpic);
    }

    @Override
    public void upDateSubTask(SubTask newSubTask) {
        subTasks.put(newSubTask.getIdentifier(), newSubTask);

        upDataStatusEpic(epics.get(newSubTask.getIdMasterTask()));
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        for (Integer integer : epics.get(id).getSubTask()) {
            subTasks.remove(integer);
        }

        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        int identifierMasterEpic = subTasks.get(id).getIdMasterTask();

        epics.get(identifierMasterEpic).removeSubTask(subTasks.get(id));
        subTasks.remove(id);
        upDataStatusEpic(epics.get(identifierMasterEpic));
    }

    @Override
    public ArrayList<SubTask> getSubTaskSpecificEpic(Integer id) {
        ArrayList<SubTask> subTasksSpecialEpic = new ArrayList<>();

        for (Integer integer : epics.get(id).getSubTask()) {
            subTasksSpecialEpic.add(subTasks.get(integer));
            upDataHistory(subTasks.get(integer));
        }

        return subTasksSpecialEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void upDataHistory(Task task) {
        historyManager.add(task);
    }

    private void upDataHistory(Collection<? extends Task> tasks) {
        for (Task task : tasks) {
            historyManager.add(task);
        }
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
