import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private Integer idMasterTask;

    public SubTask(
            String name,
            String description,
            int idMasterTask,
            Variety variety
    ) {
        super(name, description, variety);
        this.idMasterTask = idMasterTask;
    }
    public SubTask(
            String name,
            String description,
            int idMasterTask,
            Status status,
            Variety variety
    ) {
        super(name, description, status, variety);
        this.idMasterTask = idMasterTask;
    }

    public SubTask(
            String name,
            String description,
            int idMasterTask,
            Status status,
            Variety variety,
            Duration duration,
            LocalDateTime startTime
    ) {
        super(name, description, status, variety, duration, startTime);
        this.idMasterTask = idMasterTask;
    }

    public Task setIdMasterTask(Task epic) {
        if (Variety.EPIC.equals(epic.getVariety())) {
            idMasterTask = epic.getIdentifier();
            return epic;
        }
        return null;
    }

    public void removeIdMasterTask(Integer idMasterTask) {
        this.idMasterTask = idMasterTask;
    }

    public Integer getIdMasterTask() {
        if (idMasterTask != null) {
            return idMasterTask;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(idMasterTask, subTask.idMasterTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode() * 31, idMasterTask * 31);
    }

    @Override
    public String toString() {
        return super.toString() + "," + idMasterTask;
    }
}
