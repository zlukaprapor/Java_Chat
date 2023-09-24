package my.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer implements Runnable {
    private static final Logger logger = LogManager.getLogger(ChatServer.class);
    private Map<Integer, Socket> mapClient = new TreeMap<Integer, Socket>();
    private ServerSocket server;

    @Override
    public void run() {
        try {
            server = new ServerSocket(8887);
            System.out.println("Server started. Waiting for clients.");
            int numberClient = 1;


            while (true) {
                Socket client = null;
                try {
                    client = server.accept();
                    Thread clientThread = new Thread(new ClientThread(client, this, numberClient));
                    clientThread.setDaemon(true);
                    clientThread.start();
                    mapClient.put(numberClient, client);
                    numberClient++;
                } catch (IOException e) {
                    logger.error("An error occurred while accepting a client: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.error("An error occurred while starting the server: " + e.getMessage(), e);
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("An error occurred while closing the server: " + e.getMessage(), e);
                }
            }
        }
    }

    public void sendMessageForAllClient(int numberClient, String clientMessage) {
        try {
            for (Map.Entry<Integer, Socket> entry : mapClient.entrySet()) {
                if (entry.getKey() != numberClient) {
                    Socket socket = entry.getValue();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println("Client " + numberClient + ": " + clientMessage);
                }
            }
        } catch (IOException e) {
            logger.error("An error occurred while sending a message to clients: " + e.getMessage(), e);
        }
    }
}
