package ru.alastar.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import ru.alastar.Engine;

import java.util.Hashtable;

/**
 * Created by mick on 04.05.15.
 */
public class GClient {
    private Client instance;
    private Thread network;
    public Hashtable<Class, NetHandler> handlers = new Hashtable<Class, NetHandler>();

    public GClient(final String addr, final int tcp, final int udp) {
        network = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    instance = new com.esotericsoftware.kryonet.Client();
                    instance.start();
                    Engine.debug("Client started");
                    instance.addListener(new ClientListener(GClient.this));
                    instance.connect(100, addr, tcp,
                            udp);
                } catch (Exception e) {
                    Engine.LogException(e);
                }
            }
        });
        network.start();
    }

    public Thread getThread() {
        return network;
    }

    public void processFromServer(Connection connection, Object object) {
        try {
            handlers.get(object.getClass()).handle(connection, object);
        } catch (Exception e) {
            Engine.LogException(e);
        }
    }

    public void registerPacket(Class c, NetHandler handler) {
        try {
            this.handlers.put(c, handler);
            this.instance.getKryo().register(c);
        } catch (Exception e) {
            Engine.LogException(e);
        }
    }

    public void sendToServer(Object c, boolean tcp) {
        if (tcp) {
            instance.sendTCP(c);
        } else
            instance.sendUDP(c);
    }

    public void removePacket(Class c) {
        try {
            this.handlers.remove(c);
        } catch (Exception e) {
            Engine.LogException(e);
        }
    }
}
