package ru.alastar.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ru.alastar.Engine;

/**
 * Created by mick on 04.05.15.
 */
public class ServerListener extends Listener {

    private Kryo kryo;
    private PacketFiltering filter;
    private GServer instance;

    public ServerListener(GServer server) {
        kryo = server.getInstance().getKryo();
        instance = server;
        kryo.setAsmEnabled(true);
        kryo.setRegistrationRequired(true);
        filter = new PacketFiltering(server);
    }

    public void received(Connection connection, Object object) {
        try {
            if (filter.checkFilter(object.getClass(), connection)) {
                instance.processFromClient(connection, object);
            }
        } catch (Exception e) {
            Engine.LogException(e);
        }
    }

    @Override
    public void connected(Connection connection) {
        Engine.debug("Client[" + connection.getRemoteAddressTCP() + "] connected!");
        if (!instance.hasClient(connection)) {
            instance.addClient(connection).onConnected();
        }
    }

    @Override
    public void disconnected(Connection connection) {
        try {
            instance.removeClient(connection).onDisconnected();
            connection.close();
        } catch(NullPointerException e)
        {
            Engine.logException(e);
        }
    }

    public Kryo getKryo() {
        return kryo;
    }

    public void registerPacket(@SuppressWarnings("rawtypes") Class c,
                               boolean filtering) {
        kryo.register(c);
        filter.addFilterFor(c, filtering);
    }

    public void setPacketDelay(float d) {
        filter.packetDelay = d;
    }

    public float getPacketDelay() {
        return filter.packetDelay;
    }

}
