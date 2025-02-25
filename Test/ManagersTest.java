import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void getNotNullWhenCreatingManager() {
        Managers managers = new Managers();

        Assertions.assertNotNull(managers, "Объект менеджера должен быть готов к работе");
    }

    @Test
    void getNotNullWhenCreatingManagerHistory() {
        Managers managersHistory = new Managers();

        Assertions.assertNotNull(managersHistory, "Объект менеджера истории должен быть готов к работе");
    }
}