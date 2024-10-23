package org.nuzhd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class.getName());

    public void connectClient(int port) {
        try (
                Socket client = new Socket("localhost", port);
                PrintWriter w = new PrintWriter(client.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            logger.info("Successful connection. In order to disconnect, send 'q'");

            Thread incomingMessages = new Thread(() -> {
                while (!client.isClosed()) {
                    try {
                        String incomingMsg = reader.readLine();
                        logger.info("Received from BroadcastServer: {}", incomingMsg);
                    } catch (IOException e) {
                        logger.info("Disconnected from server");
                    }
                }
            });
            incomingMessages.start();

            String message;
            while (!(message = System.console().readLine("> ")).equals("q")) {
                w.println(message);
            }

            client.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
