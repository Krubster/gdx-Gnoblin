package ru.alastar.net;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by mick on 04.05.15.
 */
public abstract class NetHandler {
    public abstract void handle(Connection connection, Object packet);
}
