package my.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import my.server.ChatServer;

public class ClientThread implements Runnable {
    Socket clientSocket;
    ChatServer chatServer;
    int numberClient;

    public ClientThread(Socket clientSocket, ChatServer chatServer, int number) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        this.numberClient = number;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Client №" + numberClient + " connected.");
            new PrintWriter(clientSocket.getOutputStream(), true).println("Client №" + numberClient + ".");
            String clientMessage = null;

            while (true) {
                clientMessage = in.readLine();
                if (!"exit".equals(clientMessage)) {
                    System.out.println("Client №" + numberClient + ": " + clientMessage);
                    chatServer.sendMessageForAllClient(numberClient, clientMessage);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
