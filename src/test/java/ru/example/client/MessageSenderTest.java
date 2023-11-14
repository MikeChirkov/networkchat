package ru.example.client;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.Mockito.*;

class MessageSenderTest {
    @Test
    public void run_exit() throws IOException {
        Client client = mock(Client.class);
        BufferedReader br = mock(BufferedReader.class);
        when(br.readLine()).thenReturn("/exit");

        MessageSender messageSender = new MessageSender(client, br);
        messageSender.run();

        verify(client, times(1)).sendMsg(any());
        verify(client, times(1)).closeClient();
    }
}