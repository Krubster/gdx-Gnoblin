package ru.alastar.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import ru.alastar.Engine;

import java.net.InetSocketAddress;
import java.util.Hashtable;

/**
 * Created by mick on 04.05.15.
 */
public class GServer {
    private Server instance;
    private Hashtable<Integer, ConnectedClient> clients = new Hashtable<Integer, ConnectedClient>();
    private Thread network;
    public Hashtable<Class, NetHandler> handlers = new Hashtable<Class, NetHandler>();

    public GServer(final String ADDR, final int TCP, final int UDP) {
        Engine.debug("Server start: " + ADDR + ":" + TCP + "/" + UDP);
        network = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    instance = new Server();
                    instance.start();
                    instance.bind(new InetSocketAddress(ADDR, TCP), new InetSocketAddress(ADDR, UDP));
                    Engine.debug("Server started at: " + ADDR + ":" + TCP + "/" + UDP);

                    instance.addListener(new ServerListener(GServer.this));
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

    public boolean hasClient(Connection connection) {
        for (ConnectedClient c : clients.values()) {
            if (c.connection.getRemoteAddressTCP().getHostName().equals(connection.getRemoteAddressTCP().getHostName())) {
                return true;
            }
        }
        return false;
    }

    public ConnectedClient addClient(Connection connection) {
        ConnectedClient cl = new ConnectedClient(connection);
        clients.put(connection.getID(), cl);
        return cl;
    }


    public ConnectedClient removeClient(Connection connection) {
        if (hasClient(connection)){
            ConnectedClient cl = clients.get(getIdByConnection(connection));
            clients.remove(getIdByConnection(connection));
	    return cl;
	}
	return null;
    }

    private Integer getIdByConnection(Connection connection) {
        for (Integer id : clients.keySet()) {
            if (clients.get(id).connection.getEndPoint().equals(connection)) {
                return id;
            }
        }
        return 0;
    }

    public ConnectedClient getClient(Connection conn) {
        for (ConnectedClient c : clients.values()) {
            if (c.connection.getEndPoint().equals(conn.getEndPoint())) {
                return c;
            }
        }
        return null;
    }

    public Server getInstance() {
        return instance;
    }

    public void sendAllExcept(Connection conn, Object c, boolean tcp) {
        if (tcp) {
            instance.sendToAllExceptTCP(conn.getID(), c);
        } else {
            instance.sendToAllExceptUDP(conn.getID(), c);
        }
    }

    public void sendAll(Object c, boolean tcp) {
        if (tcp) {
            instance.sendToAllTCP(c);
        } else {
            instance.sendToAllUDP(c);
        }
    }


    public void sendToClient(Connection conn, Object c, boolean tcp) {
        if (tcp) {
            instance.sendToTCP(conn.getID(), c);
        } else {
            instance.sendToUDP(conn.getID(), c);
        }
    }

    public void processFromClient(Connection connection, Object object) {
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

    public void removePacket(Class c) {
        try {
            this.handlers.remove(c);
        } catch (Exception e) {
            Engine.LogException(e);
        }
    }
}
