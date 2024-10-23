package org.nuzhd;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new BroadcastServer()).execute(args);
    }
}