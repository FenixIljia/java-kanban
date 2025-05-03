package Manager;

import History.HistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {
    @Test
    void getNotNullWhenCreatingManager() {
        Managers managers = new Managers();

        Assertions.assertNotNull(managers, "Объект менеджера должен быть готов к работе");
    }

    @Test
    void getNotNullWhenCreatingManagerHistory() {
        HistoryManager managersHistory = Managers.getDefaultHistory();

        Assertions.assertNotNull(managersHistory, "Объект менеджера истории должен быть готов к работе");
    }
}