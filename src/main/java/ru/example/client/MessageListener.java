package ru.example.client;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageListener implements Runnable {
    private final Client client;
    private final BufferedReader inMessage;

    public MessageListener(Client client, BufferedReader inMessage) throws IOException {
        this.inMessage = inMessage;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = inMessage.readLine();
                if (message == null) {
                    client.closeClient();
                    break;
                }else{
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            client.closeClient();
        }
    }
}
