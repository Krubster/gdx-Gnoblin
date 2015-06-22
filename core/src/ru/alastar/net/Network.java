package ru.alastar.net;

/**
 * Created by mick on 04.05.15.
 */
public class Network {


    public static GServer startServer(final String ADDR, final int TCP, final int UDP) {
        return new GServer(ADDR, TCP, UDP);
    }

    public static GClient startClient(String ADDR, int TCP, int UDP) {
        return new GClient(ADDR, TCP, UDP);
    }
}
