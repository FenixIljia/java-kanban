import java.util.ArrayList;
import java.util.List;

interface TaskManager {
    Task addTask(Task task);

    Task addEpic(Epic epic);

    Task addSubTask(SubTask subTask);

    ArrayList<Epic> getAllEpics();

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    void clearAllTasks();

    void clearAllEpic();

    void clearAllSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void upDateTask(Task newTask);

    void upDateEpic(Epic newEpic);

    void upDateSubTask(SubTask newSubTask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<SubTask> getSubTaskSpecificEpic(Integer id);

    List<Task> getHistory();
}
