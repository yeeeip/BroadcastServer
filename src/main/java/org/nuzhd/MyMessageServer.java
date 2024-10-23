package org.nuzhd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyMessageServer {

    private static final Logger logger = LoggerFactory.getLogger(MyMessageServer.class.getName());
    private final ConnectionHolder connectionHolder = ConnectionHolder.getInstance();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public MyMessageServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.error("Unable to create ServerSocket on port {}", port, e);
        }
    }

    public void start() {
        logger.info("Started on port {}", serverSocket.getLocalPort());

        while (true) {
            try {
                Socket s = serverSocket.accept();
                String clientAddr = String.format("%s:%s", s.getInetAddress().getHostAddress(), s.getPort());
                connectionHolder.addConnection(s);

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                executorService.execute(() ->
                {
                    logger.info("Client connected: {}", clientAddr);
                    try {

                        String message;
                        while ((message = reader.readLine()) != null) {
                            logger.info("Received message from {}: {}", clientAddr, message);
                            broadcast(message, s);
                        }

                        connectionHolder.removeConnection(s);
                    } catch (IOException e) {
                        logger.error("Exception occurred with {}", clientAddr, e);
                    }
                    logger.info("Client {} disconnected!", clientAddr);
                });
            } catch (IOException e) {
                logger.error("Exception occurred while accepting connection: {}", e.getMessage());
            }
        }
    }

    public void broadcast(String message, Socket exclude) {
        for (Socket client : connectionHolder.getConnections()) {
            try {
                if (!client.equals(exclude)) {
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    writer.println(message);
                }
            } catch (IOException e) {
                logger.error("Unable to broadcast message to {}", client.getPort(), e);
            }
        }
    }
}
