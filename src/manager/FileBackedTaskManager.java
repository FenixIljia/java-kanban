package manager;

import data.*;
import exception.ManagerIntersectionException;
import exception.ManagerSaveException;
import history.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    Path file;
    Path fileHistory;

    FileBackedTaskManager(Path file, Path fileHistory) {
        if (Files.exists(file)) {
            this.file = file;
        } else {
            try {
                this.file = Files.createFile(file);
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        }

        if (Files.exists(fileHistory)) {
            this.fileHistory = fileHistory;
        } else {
            try {
                this.fileHistory = Files.createFile(fileHistory);
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        }
    }

    // Метод восстановления данных из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(Path file, Path fileHistory) throws ManagerIntersectionException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file, fileHistory);
        int count = 0;
        fileBackedTaskManager.setHistoryManager(fromIsFileHistory(fileHistory));

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
                        fileBackedTaskManager.getEpics().put(task.getIdentifier(), (Epic) task);
                        fileBackedTaskManager.setIdentifier(count++);
                    } else if (task.getVariety().equals(Variety.SUBTASK)) {
                        fileBackedTaskManager.getSubTasks().put(task.getIdentifier(), (SubTask) task);
                        fileBackedTaskManager.setIdentifier(count++);
                        fileBackedTaskManager.getEpic(((SubTask) task).getIdMasterTask()).addSubTask(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        fileBackedTaskManager.getPrioritizedTasksAtLoading();
        return fileBackedTaskManager;
    }

    // создает обект типа Data.Task из строки полученную из файла
    private static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String[] line = value.split(",");

        if (line[0].equals("id")) {
            return null;
        }

        if (line.length < 5) {
            Epic epic = new Epic(line[2], line[3], Variety.EPIC);
            epic.setIdentifier(Integer.parseInt(line[0]));
            return epic;
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
            SubTask subTask = new SubTask(
                    line[2],
                    line[4],
                    Integer.parseInt(line[8]),
                    status,
                    variety,
                    Duration.ofMinutes(Long.parseLong(line[6])),
                    LocalDateTime.parse(line[5], formatter)
            );
            subTask.setIdentifier(Integer.parseInt(line[0]));
            return subTask;
        } else if (variety.equals(Variety.EPIC)) {
            Epic epic = new Epic(
                    line[2],
                    line[4],
                    variety
            );
            epic.setDuration(Duration.ofMinutes(Long.parseLong(line[6])));
            epic.setStartTime(LocalDateTime.parse(line[5], formatter));
            epic.setIdentifier(Integer.parseInt(line[0]));
            return epic;
        } else if (variety.equals(Variety.TASK)) {
            Task task = new Task(
                    line[2],
                    line[4],
                    status,
                    variety,
                    Duration.ofMinutes(Long.parseLong(line[6])),
                    LocalDateTime.parse(line[5], formatter)
            );
            task.setIdentifier(Integer.parseInt(line[0]));
            return task;
        }

        return null;
    }

    @Override
    public Task addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Task addTask(Task task) throws ManagerIntersectionException {
        super.addTask(task);
        save();
        return task;
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
    public Task addSubTask(SubTask subTask) throws ManagerIntersectionException {
        super.addSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void upDateSubTask(SubTask newSubTask) throws ManagerIntersectionException {
        super.upDateSubTask(newSubTask);
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

    @Override
    public void upDateTask(Task newTask) throws ManagerIntersectionException {
        super.upDateTask(newTask);
        save();
    }

    private static HistoryManager fromIsFileHistory(Path fileHistory) {
        HistoryManager historyManager = Managers.getDefaultHistory();

        try (Reader reader = new FileReader(fileHistory.toFile())) {
            BufferedReader bufferedReader = new BufferedReader(reader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                historyManager.add(fromString(line));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

        return historyManager;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Task getTask(int id) throws NullPointerException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    // сохраняет текущее состояние менеджера в указаный файл
    private void save() {

        try (Writer fileWriter = new FileWriter(file.toFile())) {
            fileWriter.write("id,type,name,status,description,startTime,duration,endTime,epic\n");

            for (Task task : getAllTasks()) {
                fileWriter.write(String.format(task + "\n"));
            }

            for (Epic task : getAllEpics()) {
                if (!task.getSubTask().isEmpty() || task.getStartTime() != null || task.getEndTime() != null) {
                    fileWriter.write(String.format(task + "\n"));
                } else {
                    fileWriter.write(String.format("%d,%s,%s,%s\n", task.getIdentifier(), task.getVariety(), task.getName(), task.getDescription()));
                }
            }

            for (SubTask task : getAllSubTasks()) {
                fileWriter.write(String.format(task + "," + task.getIdMasterTask() + "\n"));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    //сохраняет историю просмотра задач
    private void saveHistory() {
        if (getIsEmptyHistory() == 0) {
            return;
        }
        try (Writer fileWrite = new FileWriter(fileHistory.toFile())) {
            for (Task task : getHistory()) {
                if (task instanceof Epic epic) {
                    if (epic.getStartTime() == null || epic.getEndTime() == null) {
                        fileWrite.write(String.format(
                                "%d,%s,%s,%s\n",
                                epic.getIdentifier(),
                                epic.getVariety(),
                                epic.getName(),
                                epic.getDescription()
                        ));
                    } else {
                        fileWrite.write(String.format(task + "\n"));
                    }
                } else {
                    fileWrite.write(String.format(task.toString() + "\n"));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
