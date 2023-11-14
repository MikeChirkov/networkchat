package ru.example.server;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ClientListenerTest {

    @Test
    public void run_exit() throws IOException {
        Server server = mock(Server.class);
        Socket socket = mock(Socket.class);
        BufferedReader br = mock(BufferedReader.class);
        BufferedWriter bw = mock(BufferedWriter.class);
        when(br.readLine()).thenReturn("/exit");

        ClientListener clientListener = new ClientListener(socket, server, bw, br);
        clientListener.run();

        verify(server, times(2)).printMessage(any());
        verify(server, times(1)).removeClient(any());
    }
}