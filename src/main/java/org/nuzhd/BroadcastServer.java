package org.nuzhd;

import picocli.CommandLine;

@CommandLine.Command(mixinStandardHelpOptions = true)
public class BroadcastServer {

    @CommandLine.Option(names = {"-p", "--port"}, defaultValue = "5050", description = "Port on which the server will listen",
            required = true)
    private int port;
    private final ConnectionManager connectionManager = new ConnectionManager();

    @CommandLine.Command(name = "start")
    int start() {
        MyMessageServer server = new MyMessageServer(port);
        server.start();
        return 0;
    }

    @CommandLine.Command(name = "connect")
    int connect() {
        connectionManager.connectClient(port);
        return 0;
    }
}
