public class SubTask extends Task {
    private Integer idMasterTask;

    public SubTask(String name, String description, int idMasterTask, Variety variety) {
        super(name, description, variety);
        this.idMasterTask = idMasterTask;
    }

    public SubTask(String name, String description, int idMasterTask, Status status, Variety variety) {
        super(name, description, status, variety);
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
    public String toString() {
        return "SubTask{" +
                "idMasterTask=" + idMasterTask +
                '}';
    }
}
