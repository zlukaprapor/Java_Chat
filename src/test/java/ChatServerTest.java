import my.server.ChatServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServerTest {
    private ChatServer chatServer;
    private static final Logger logger = LogManager.getLogger(ChatServerTest.class);

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
        assertDoesNotThrow(() -> {
            Thread serverThread = new Thread(chatServer);
            serverThread.start();
            // Sleep for a while to allow the server to start
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread sleep interrupted: " + e.getMessage(), e);
            }
            serverThread.interrupt();
        });
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
            logger.error("Client connection failed: " + e.getMessage(), e);
            fail("Client connection failed: " + e.getMessage());
        }

        // Stop the server
        serverThread.interrupt();
    }
}
