package ru.alastar.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by mick on 04.05.15.
 */
public class ClientListener extends Listener {
    private Kryo kryo;
    //    private PacketFiltering filter;
    private GClient instance;

    public ClientListener(GClient gClient) {
        this.instance = gClient;
    }

    public void received(Connection connection, Object object) {
        instance.processFromServer(connection, object);
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        connection.close();
    }

    public Kryo getKryo() {
        return kryo;
    }

}
