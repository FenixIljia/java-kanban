import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private Integer identifier = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> sortPrioritizedTasks = new TreeSet<>((Task task1, Task task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else if (task2.getStartTime().isBefore(task1.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    }); //список задач отсоритрованный по приоритету

    @Override
    public Task addTask(Task task) throws ManagerSaveException {
        if (task.getVariety().equals(Variety.TASK)) {
            task.setIdentifier(generationIdentifier());
            tasks.put(task.getIdentifier(), task);
            addInSortPrioritizedTasks(task);
            return task;
        } else {
            return null;
        }
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
    public Task addSubTask(SubTask subTask) throws ManagerSaveException {
        if (Variety.SUBTASK.equals(subTask.getVariety())) {
            subTask.setIdentifier(generationIdentifier());


            subTasks.put(subTask.getIdentifier(), subTask);

            epics.get(subTask.getIdMasterTask()).addSubTask(subTask);

            upDataStatusEpic(epics.get(subTask.getIdMasterTask()));

            addInSortPrioritizedTasks(subTask);

            return subTask;
        }
        return null;

    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
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
            epic.clearSubTasksEpic();
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
    public void upDateTask(Task newTask) throws ManagerSaveException {
        addInSortPrioritizedTasks(newTask);
        tasks.put(newTask.getIdentifier(), newTask);
    }

    @Override
    public void upDateEpic(Epic newEpic) {
        epics.put(newEpic.getIdentifier(), newEpic);
    }

    @Override
    public void upDateSubTask(SubTask newSubTask) throws ManagerSaveException {
        addInSortPrioritizedTasks(newSubTask);
        subTasks.put(newSubTask.getIdentifier(), newSubTask);
        upDataStatusEpic(epics.get(newSubTask.getIdMasterTask()));
    }

    @Override
    public void removeTask(int id) {
        sortPrioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        for (SubTask subTask : epics.get(id).getSubTask()) {
            historyManager.remove(subTask.getIdentifier());
            subTasks.remove(subTask.getIdentifier());
        }

        epics.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void removeSubtask(int id) {
        int identifierMasterEpic = subTasks.get(id).getIdMasterTask();
        sortPrioritizedTasks.remove(subTasks.get(id));

        epics.get(identifierMasterEpic).removeSubTask(subTasks.get(id));
        subTasks.get(id).removeIdMasterTask(null);
        subTasks.remove(id);
        upDataStatusEpic(epics.get(identifierMasterEpic));
        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> getSubTaskSpecificEpic(Integer id) {
        return new ArrayList<>(epics.get(id).getSubTask());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return sortPrioritizedTasks;
    }

    protected HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    protected void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    protected HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    private void upDataHistory(Task task) {
        historyManager.add(task);
    }

    private void upDataStatusEpic(Epic epic) {
        int variableForCountingDone = 0;
        int variableForCountingNew = 0;

        if (epic.getSubTask().isEmpty()) {
            return;
        }

        for (SubTask subTask : epic.getSubTask()) {
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if (subTask.getStatus() == Status.DONE) {
                variableForCountingDone++;

                epic.setStatus(Status.IN_PROGRESS);

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

    //проверка на пересечение задач
    private boolean intersectionCheck(Task task1, Task task2) {
        return !(task1.getEndTime().isBefore(task2.getStartTime()) || task2.getEndTime().isBefore(task1.getStartTime()));
    }

    //Добавление задачи в список sortPrioritizedTasks
    private void addInSortPrioritizedTasks(Task task) throws ManagerSaveException {
        if (sortPrioritizedTasks.isEmpty()) {
            sortPrioritizedTasks.add(task);
            return;
        }

        if (task.getStartTime() == null) {
            return;
        }

        boolean hasIntersection = sortPrioritizedTasks.stream()
                .filter(existingTask -> existingTask.getStartTime() != null)
                .filter(existingTask -> !(existingTask.getIdentifier() == task.getIdentifier()))
                .anyMatch(existingTask -> intersectionCheck(existingTask, task));

        if (hasIntersection) {
            throw new ManagerSaveException();
        }

        sortPrioritizedTasks.add(task);
    }
}

