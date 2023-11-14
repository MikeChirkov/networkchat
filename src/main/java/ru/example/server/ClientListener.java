package ru.example.server;

import java.io.*;
import java.net.Socket;

public class ClientListener implements Runnable {

    private static int clientsCount = 0;
    private Server server;
    private BufferedWriter outMessage;
    private BufferedReader inMessage;
    private Socket clientSocket;
    private String clientName = "";

    public ClientListener(Socket socket, Server server, BufferedWriter bw, BufferedReader br) {
        clientsCount++;
        this.server = server;
        this.clientSocket = socket;
        this.outMessage = bw;
        this.inMessage = br;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String clientMessage = inMessage.readLine();
                if (clientMessage == null || clientSocket.isClosed()) {
                    break;
                }
                if (clientMessage.contains("/exit")) {
                    break;
                }
                if (clientMessage.contains("clientName###")) {
                    clientName = clientMessage.split("###")[1];
                    server.printMessage("Новый участник «" + clientName + "» вошёл в чат!");
                    server.printMessage("Участников в чате - " + clientsCount + " :)");
                    continue;
                }
                server.printMessage(clientMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.close();
        }
    }

    public void sendMsg(String message) {
        try {
            outMessage.write(message + '\n');
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        server.removeClient(this);
        clientsCount--;
        try {
            server.printMessage("Участник «" + clientName + "» вышел из чата");
            server.printMessage("Участников в чате - " + clientsCount + " :(");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
