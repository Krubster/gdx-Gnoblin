package ru.alastar.utils;

import ru.alastar.Engine;
import ru.alastar.log.LogLevel;

import java.util.Hashtable;

/**
 * Created by mick on 29.04.15.
 */
public class ObjectPooler {

    public static Hashtable<Class, PoolArray> pool = new Hashtable<Class, PoolArray>();
    private static int standardPool = 15;

    public static void freeObject(Class c, Object o) {
        checkPool(c);
        pool.get(c).add(o);
        Engine.Log(LogLevel.FIRST, "POOL", "Object " + c.toString() + " was successfully placed at pool!");
    }

    public static Object getFreeObject(Class c) {
        if (haveObjectsOfType(c)) {
            return getPool(c).getObject();
        }
        return null;
    }

    public static PoolArray createPoolOfType(Class c) {
        return checkPool(c);
    }

    public static void extendPoolAndFill(Class c, int amt) {
        if (haveObjectsOfType(c))
            getPool(c).extendAndFill(amt, c);
    }

    public static void extendPool(Class c, int amt) {
        Engine.Log(LogLevel.FIRST, "POOL", "Extending pool at " + amt);
        if (haveObjectsOfType(c))
            getPool(c).extend(amt);
    }


    public int getMemoryPooled(Class c) {
        if (haveObjectsOfType(c))
            return getPool(c).getAllocated(c);
        return 0;
    }

    public int getTotalMemoryPooled() {
        int total = 0;
        for (Class c : pool.keySet()) {
            total += getPool(c).getAllocated(c);
        }
        return total;
    }

    public static PoolArray getPool(Class c) {
        return pool.get(c);
    }

    public static boolean haveObjectsOfType(Class c) {
        return pool.containsKey(c);
    }

    public static boolean hasObjects(Class c) {
        if (haveObjectsOfType(c)) {
            Engine.Log(LogLevel.FIRST, "POOL", "Pool of type " + c.toString() + " exists in pooler");
            return getPool(c).haveObject();
        } else
            return false;
    }

    public static int getPooledCount() {
        int count = 0;
        for (Class c : pool.keySet()) {
            for (Object o : pool.get(c).getCollection()) {
                ++count;
            }
        }
        return count;
    }

    private static PoolArray checkPool(Class c) {
        PoolArray pa = null;
        if (!pool.containsKey(c)) {
            pa = new PoolArray(standardPool);
            pool.put(c, pa);
        }
        return pa;
    }
}
