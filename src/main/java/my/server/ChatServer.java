package my.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;


public class ChatServer implements Runnable {
    private  Map<Integer, Socket> mapClient = new TreeMap<Integer, Socket>();
    private  ServerSocket server;

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
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (server != null && !server.isClosed()){
                try {
                    server.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageForAllClient(int numberClient, String clientMessage) {
        System.out.println("Message readdressing is not implemented");

    }
}
