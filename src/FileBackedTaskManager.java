import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    Path file;

    FileBackedTaskManager(Path file) {
        if (Files.exists(file)) {
            this.file = file;
        } else {
            try {
                this.file = Files.createFile(file);
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        }
    }

    // Метод восстановления данных из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int count = 0;
        ArrayList<Epic> epic = new ArrayList<>();
        ArrayList<SubTask> subTask = new ArrayList<>();

        try (Reader fileReader = new FileReader(file.toFile())) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                Task task = fromString(line);
                if (task != null) {

                    if (task.getVariety().equals(Variety.TASK)) {
                        fileBackedTaskManager.getTasks().put(task.getIdentifier(), task);
                        fileBackedTaskManager.setIdentifier(count++);
                    } else if (task.getVariety().equals(Variety.EPIC)) {
                        epic.add((Epic) task);
                    } else if (task.getVariety().equals(Variety.SUBTASK)) {
                        subTask.add((SubTask) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        for (Epic epicInFor : epic) {
            fileBackedTaskManager.getEpics().put(epicInFor.getIdentifier(), epicInFor);
            fileBackedTaskManager.setIdentifier(count++);
        }

        for (SubTask subTaskInFor : subTask) {
            fileBackedTaskManager.getSubTasks().put(subTaskInFor.getIdentifier(), subTaskInFor);
            fileBackedTaskManager.getEpic(subTaskInFor.getIdMasterTask()).addSubTask(subTaskInFor);
            fileBackedTaskManager.setIdentifier(count++);

        }

        return fileBackedTaskManager;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Task addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Task addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void upDateEpic(Epic newEpic) {
        super.upDateEpic(newEpic);
        save();
    }

    @Override
    public void upDateSubTask(SubTask newSubTask) {
        super.upDateSubTask(newSubTask);
        save();
    }

    @Override
    public void upDateTask(Task newTask) {
        super.upDateTask(newTask);
        save();
    }

    @Override
    public void clearAllEpic() {
        super.clearAllEpic();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    // сохраняет текущее состояние менеджера в указаный файл

    public static void main(String[] args) {
        Path file1 = Paths.get(
                "D:\\Программирование\\Проекты на JavaScript\\Яндекс Практикум\\Проект Java Practicum\\Test.txt"
        );
        FileBackedTaskManager manager = new FileBackedTaskManager(file1);
        boolean taskBoolean = false;
        boolean epicBoolean = false;
        boolean subTaskBoolean = false;

        Task task1 = new Task("Test1", "Task1", Status.NEW, Variety.TASK);
        Task task2 = new Task("Test2", "Task2", Status.NEW, Variety.TASK);
        Epic epic1 = new Epic("Test3", "Epic1", Status.NEW, Variety.EPIC);
        Epic epic2 = new Epic("Test4", "Epic2", Status.NEW, Variety.EPIC);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Test5", "SubTask1 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask2 = new SubTask("Test6", "SubTask2 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);
        SubTask subTask3 = new SubTask("Test7", "SubTask3 in Epic 1", epic1.getIdentifier(), Status.NEW, Variety.SUBTASK);

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        Task task3 = new Task("Test3", "Task3", Status.NEW, Variety.TASK);

        manager.addTask(task3);


        FileBackedTaskManager fileBackedTaskManager = loadFromFile(file1);

        for (Task task : manager.getAllTasks()) {
            for (Task taskBacked : fileBackedTaskManager.getAllTasks()) {
                if (task.equals(taskBacked)) {
                    taskBoolean = true;
                    break;
                } else {
                    taskBoolean = false;
                }
            }

            if (!taskBoolean) {
                break;
            }
        }

        for (Epic task : manager.getAllEpics()) {
            for (Epic taskBacked : fileBackedTaskManager.getAllEpics()) {
                if (task.equals(taskBacked)) {
                    epicBoolean = true;
                    break;
                } else {
                    epicBoolean = false;
                }
            }

            if (!epicBoolean) {
                break;
            }
        }

        for (SubTask task : manager.getAllSubTasks()) {
            for (SubTask taskBacked : fileBackedTaskManager.getAllSubTasks()) {
                if (task.equals(taskBacked)) {
                    subTaskBoolean = true;
                    break;
                } else {
                    subTaskBoolean = false;
                }
            }

            if (!subTaskBoolean) {
                break;
            }
        }

        System.out.println(taskBoolean);
        System.out.println(epicBoolean);
        System.out.println(subTaskBoolean);
    }

    private void save() {

        try (Writer fileWriter = new FileWriter(file.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                fileWriter.write(String.format("%d,%s\n", task.getIdentifier(), task));
            }

            for (Task task : getAllEpics()) {
                fileWriter.write(String.format("%d,%s\n", task.getIdentifier(), task));
            }

            for (Task task : getAllSubTasks()) {
                fileWriter.write(String.format("%d,%s\n", task.getIdentifier(), task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    // создает обект типа Task из строки полученную из файла
    private static Task fromString(String value) {
        String[] line = value.split(",");

        if (line[0].equals("id")) {
            return null;
        }

        Status status = null;
        Variety variety = null;

        status = switch (line[3]) {
            case "NEW" -> Status.NEW;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            case "DONE" -> Status.DONE;
            default -> status;
        };

        variety = switch (line[1]) {
            case "TASK" -> Variety.TASK;
            case "EPIC" -> Variety.EPIC;
            case "SUBTASK" -> Variety.SUBTASK;
            default -> variety;
        };

        if (variety.equals(Variety.SUBTASK)) {
            SubTask subTask = new SubTask(line[2], line[4], Integer.parseInt(line[5]), status, variety);
            subTask.setIdentifier(Integer.parseInt(line[0]));
            return subTask;
        } else if (variety.equals(Variety.EPIC)) {
            Epic epic = new Epic(line[2], line[4], status, variety);
            epic.setIdentifier(Integer.parseInt(line[0]));
            return epic;
        } else if (variety.equals(Variety.TASK)) {
            Task task = new Task(line[2], line[4], status, variety);
            task.setIdentifier(Integer.parseInt(line[0]));
            return task;
        }

        return null;
    }
}
