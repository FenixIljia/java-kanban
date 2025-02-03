public class Task {
    private final String name;
    private final String description;
    private int identifier;
    private Status status = Status.NEW;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
        return identifier == task.identifier;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identifier=" + identifier +
                ", status=" + status +
                '}';
    }

/*    @Override
    public int hashCode() {
        int hash = 17;

        if (name != null) {
            hash = hash + name.hashCode();
        }

        hash *= 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }

        hash = hash * 31 + Objects.hashCode(identifier);

        return hash;
    }*/
}
