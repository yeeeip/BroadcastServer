package org.nuzhd;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionHolder {

    private static ConnectionHolder instance;
    private final CopyOnWriteArrayList<Socket> connections = new CopyOnWriteArrayList<>();

    private ConnectionHolder() {
    }

    public static synchronized ConnectionHolder getInstance() {
        if (instance == null) {
            instance = new ConnectionHolder();
        }
        return instance;
    }

    public void addConnection(Socket s) {
        this.connections.add(s);
    }

    public List<Socket> getConnections() {
        return connections;
    }

    public void removeConnection(Socket s) {
        this.connections.remove(s);
    }
}
