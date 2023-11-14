package ru.example.server;

import ru.example.utils.Constants;
import ru.example.utils.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Server {
    private final ArrayList<ClientListener> clients = new ArrayList<>();
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    public Server() {
        try {
            Map<String, String> config = FileUtils.getConfig(Constants.CONFIG_PATH);
            serverSocket = new ServerSocket(Integer.parseInt(config.get("port")));
            printLocalMessage("Сервер запущен!");
            while(true){
                clientSocket = serverSocket.accept();
                ClientListener client = new ClientListener(clientSocket, this,
                        new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),
                        new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                printLocalMessage("Сервер остановлен");
                closeServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String message) {
        for (ClientListener o : clients) {
            o.sendMsg(message);
        }
    }

    public void printMessage(String message) throws IOException {
        printLocalMessage(message);
        sendMessageToAllClients(message);
    }

    public void printLocalMessage(String message) throws IOException {
        String msg = String.format("[%s] - %s \n", DATE_FORMAT.format(new Date()), message);
        System.out.print(msg);
        FileUtils.writeToFileLog(msg, Constants.FULL_LOG_PATH);
    }

    public void removeClient(ClientListener client) {
        clients.remove(client);
    }

    public void closeServer() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                for (ClientListener cl: clients) {
                    if(!cl.getClientSocket().isClosed()){
                        cl.getClientSocket().close();
                        cl.close();
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
}