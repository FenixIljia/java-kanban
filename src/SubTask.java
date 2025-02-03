public class SubTask extends Task {
    private final int idMasterTask;

    public SubTask(String name, String description, int idMasterTask) {
        super(name, description);
        this.idMasterTask = idMasterTask;
    }

    public SubTask(String name, String description, int idMasterTask, Status status) {
        super(name, description, status);
        this.idMasterTask = idMasterTask;
    }

    public int getIdMasterTask() {
        return idMasterTask;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "idMasterTask=" + idMasterTask +
                '}';
    }
}
