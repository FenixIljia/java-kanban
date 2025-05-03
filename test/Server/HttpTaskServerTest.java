package Server;

import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;

    @BeforeEach
    public void startServerInTest() {
        TaskManager taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.startServer();
    }

    @AfterEach
    public void stopServerInTest() {
        httpTaskServer.stopServer();
    }

    @Test
    public void getNotNullAtStartServer() {
        Assertions.assertNotNull(httpTaskServer);
    }
}
