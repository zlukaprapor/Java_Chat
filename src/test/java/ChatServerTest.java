import my.server.ChatServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServerTest {
    private ChatServer chatServer;

    @BeforeEach
    public void setUp() {
        chatServer = new ChatServer();
    }

    @AfterEach
    public void tearDown() {
        chatServer = null;
    }

    @Test
    public void testServerSocketInitialization() {
        assertDoesNotThrow(ChatServer::new);
    }

    @Test
    public void testClientConnection() {
        Thread serverThread = new Thread(chatServer);
        serverThread.start();

        // Simulate a client connection
        try {
            Socket clientSocket = new Socket("localhost", 8887);
            assertTrue(clientSocket.isConnected());
            clientSocket.close();
        } catch (IOException e) {
            fail("Client connection failed: " + e.getMessage());
        }

        // Stop the server
        serverThread.interrupt();
    }
}
