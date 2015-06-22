package ru.alastar.net;

import com.esotericsoftware.kryonet.Connection;
import ru.alastar.Engine;

import java.util.Date;
import java.util.Hashtable;

/**
 * Created by mick on 04.05.15.
 */
public class PacketFiltering {
    public Hashtable<Class, Boolean> filters = new Hashtable<Class, Boolean>();
    public float packetDelay = 100F;
    public GServer instance;

    public PacketFiltering(GServer instance) {
        this.instance = instance;
    }

    public void addFilterFor(Class c, boolean f) {
        filters.put(c, f);
    }

    @SuppressWarnings("unused")
    public boolean checkFilter(Class c, Connection conn) {
        for (Class cls : filters.keySet()) {
            if (c.getEnclosingClass() == cls.getEnclosingClass()) {
                if (filters.get(c) == true) {
                    ConnectedClient client = instance.getClient(conn);
                    if (c != null) {
                        if ((new Date().getTime() - client.lastPacket.getTime()) > packetDelay) {
                            client.lastPacket = new Date();
                            return true;
                        } else
                            return false;
                    } else {
                        Engine.Log("[ERROR]", "Connected client is null");
                        return false;
                    }
                } else
                    return true;
            }
        }
        return false;
    }
}
