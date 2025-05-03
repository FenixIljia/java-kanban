package Exception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(Throwable e) {
        super(e);
    }

    public ManagerSaveException() {
        super();
    }
}
