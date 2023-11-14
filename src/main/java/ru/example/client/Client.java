package ru.example.client;

import ru.example.utils.Constants;
import ru.example.utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class Client {
    private Socket clientSocket;
    private BufferedReader inMessage;
    private BufferedWriter outMessage;
    private String clientName;

    public Client() {
        try {
            Map<String, String> config = FileUtils.getConfig(Constants.CONFIG_PATH);
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите ваше имя: ");
            clientName = stdin.readLine();
            clientSocket = new Socket(config.get("host"), Integer.parseInt(config.get("port")));
            inMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            sendMsg("clientName###" + clientName);
            new Thread(new MessageListener(this, inMessage)).start();
            new Thread(new MessageSender(this, stdin)).start();
        } catch (IOException e) {
            closeClient();
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return this.clientName;
    }

    public void closeClient() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                inMessage.close();
                outMessage.close();
            }
        } catch (IOException ignored) {
        }
    }

    public void sendMsg(String message) throws IOException {
        outMessage.write(message + '\n');
        outMessage.flush();
    }
}


