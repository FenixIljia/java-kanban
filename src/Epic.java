import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasksEpic;
    private LocalDateTime endTime;

    public Epic(
            String name,
            String description,
            Variety variety
    ) {
        super(name, description, variety);
        subTasksEpic = new ArrayList<>();
        setStartTime(null);
        setDuration(null);
        endTime = null;
    }

    public Task addSubTask(Task subTask) {
        if (Variety.SUBTASK.equals(subTask.getVariety())) {
            subTasksEpic.add((SubTask) subTask);
            calculationStartTime();
            calculationDuration();
            getEndTime();
            return subTask;
        }
        return null;
    }

    public ArrayList<SubTask> getSubTask() {
        return subTasksEpic;
    }

    public void clearSubTasksEpic() {
        subTasksEpic.clear();
        setStartTime(null);
        setDuration(Duration.ofMinutes(0));
    }

    public void removeSubTask(SubTask subTask) {
        subTasksEpic.remove(subTask);
        calculationDuration();
        calculationStartTime();
        endTime = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksEpic, epic.subTasksEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode() * 31, Objects.hash(subTasksEpic) * 31);
    }

    @Override
    public LocalDateTime getEndTime() {
        for (SubTask subTask : subTasksEpic) {
            if (endTime == null) {
                endTime = subTask.getEndTime();
            } else {
                if (subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime();
                }
            }
        }
        return endTime;
    }

    //метод расчета продолжительности эпика на основе его подзадач
    private void calculationDuration() {
        setDuration(Duration.ofMinutes(0));

        for (SubTask task : subTasksEpic) {
            if (task.getDuration().equals(Duration.ofMinutes(0))) {
                getDuration().plus(task.getDuration());
            }
        }
    }

    //метод расчета даты и времени эпика, когда предполагается приступить к задаче на основе его подзадач
    private void calculationStartTime() {
        for (SubTask subTask : subTasksEpic) {
            if (getStartTime() == null) {
                setStartTime(subTask.getStartTime());
                return;
            }
            if (subTask.getStartTime() != null) {
                if (subTask.getStartTime().isBefore(getStartTime())) {
                    setStartTime(subTask.getStartTime());
                }
            }
        }
    }
}
