package ru.alastar.net;

import com.esotericsoftware.kryonet.Connection;

import java.util.Date;

/**
 * Created by mick on 04.05.15.
 */
public class ConnectedClient {
    public Date lastPacket;

    public Connection connection;

    public ConnectedClient(Connection c) {
        this.connection = c;
        lastPacket = new Date();
    }

    public void onConnected(){}

    public void onDisconnected(){}
}
