package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import exception.ManagerIntersectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    Task addTask(Task task) throws ManagerIntersectionException;

    Task addEpic(Epic epic);

    Task addSubTask(SubTask subTask) throws ManagerIntersectionException;

    ArrayList<Epic> getAllEpics();

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    void clearAllTasks();

    void clearAllEpic();

    void clearAllSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void upDateTask(Task newTask) throws ManagerIntersectionException;

    void upDateEpic(Epic newEpic);

    void upDateSubTask(SubTask newSubTask) throws ManagerIntersectionException;

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<SubTask> getSubTaskSpecificEpic(Integer id);

    List<Task> getHistory();

    int getIsEmptyHistory();

    TreeSet<Task> getPrioritizedTasks();
}
