import com.google.gson.annotations.JsonAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int identifier;
    private Status status;
    private Variety variety;
    @JsonAdapter(DurationAdapter.class)
    private Duration duration; //продолжительность задачи
    private LocalDateTime startTime; //дата и время, когда предоплагается приступить к заданию

    public Task(
            String name,
            String description,
            Variety variety
    ) {
        this.name = name;
        this.description = description;
        this.variety = variety;
        startTime = null;
        duration = Duration.ofMinutes(0);
        this.status = Status.NEW;
    }

    public Task(
            String name,
            String description,
            Variety variety,
            int identifier
    ) {
        this.name = name;
        this.description = description;
        this.variety = variety;
        startTime = null;
        duration = Duration.ofMinutes(0);
        this.status = Status.NEW;
        this.identifier = identifier;
    }

    public Task(
            String name,
            String description,
            Status status,
            Variety variety
    ) {
        this.name = name;
        this.description = description;
        this.variety = variety;
        startTime = null;
        duration = Duration.ofMinutes(0);
        this.status = status;
    }

    public Task(
            String name,
            String description,
            Status status,
            Variety variety,
            Duration duration,
            LocalDateTime startTime
    ) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.variety = variety;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(
            String name,
            String description,
            int identifier,
            Status status,
            Variety variety,
            Duration duration,
            LocalDateTime startTime
    ) {
        this.name = name;
        this.description = description;
        this.identifier = identifier;
        this.status = status;
        this.variety = variety;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            this.startTime = null;
            return;
        }

        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Variety getVariety() {
        return variety;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getIdentifier() {
        return identifier;
    }

    public Status getStatus() {
        return status;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identifier == task.identifier
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return String.format(
                "%d,%s,%s,%s,%s,%s,%d,%s",
                identifier,
                variety.toString(),
                name,
                status.toString(),
                description,
                startTime.format(formatter),
                duration.toMinutes(),
                getEndTime().format(formatter)
        );
    }

    @Override
    public int hashCode() {
        int hash = 17;

        if (name != null) {
            hash = hash + name.hashCode();
        }

        hash *= 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }

        hash = hash * 31;

        hash += Objects.hashCode(status);

        hash *= 31;

        hash += Objects.hashCode(variety);

        return hash;
    }
}
