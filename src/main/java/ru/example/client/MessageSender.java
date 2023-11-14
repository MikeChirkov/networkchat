package ru.example.client;

import java.io.BufferedReader;

public class MessageSender implements Runnable {
    private final Client client;
    private final BufferedReader clientIn;

    public MessageSender(Client client, BufferedReader clientIn) {
        this.client = client;
        this.clientIn = clientIn;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String userWord = clientIn.readLine();
                if (userWord.equals("/exit")) {
                    client.sendMsg(String.format("%s: %s", client.getClientName(), userWord));
                    client.closeClient();
                    break;
                } else {
                    client.sendMsg(String.format("%s: %s", client.getClientName(), userWord));
                }
            }
        } catch (Exception e) {
            client.closeClient();
        }
    }
}
