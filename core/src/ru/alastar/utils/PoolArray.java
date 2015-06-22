package ru.alastar.utils;

import com.badlogic.gdx.utils.Array;
import ru.alastar.Engine;
import ru.alastar.log.LogLevel;

/**
 * Created by mick on 29.04.15.
 */
public class PoolArray {

    public Array<Object> pool = new Array<Object>();
    public int maxPooled = 10;

    public PoolArray(int max) {
        this.maxPooled = max;
    }

    public Array<Object> getCollection() {
        return pool;
    }

    public void clearPool() {
        pool.clear();
    }


    public void add(Object o) {
        if (pool.size < maxPooled) {
            pool.add(o);
        }
    }

    public boolean haveObject() {
        if (pool.size == 0) {
            Engine.Log(LogLevel.FIRST, "POOL", "There's no pooled objects");
            return false;
        } else {
            Engine.Log(LogLevel.FIRST, "POOL", "There's " + pool.size + " pooled objects");
            return true;
        }
    }

    public Object getObject() {
        if (pool.size > 0) {
            Object obj = pool.get(0);
            pool.removeValue(obj, true);
            Engine.Log(LogLevel.FIRST, "POOL", "Object  was removed from pool!");
            return obj;
        }
        return null;
    }

    public void extend(int amt) {
        this.maxPooled += amt;
    }

    public void extendAndFill(int amt, Class c) {
        try {
            this.maxPooled += amt;
            Object o;
            for (int i = pool.size; i < maxPooled; ++i) {
                o = c.newInstance();
                pool.add(o);
            }
        } catch (InstantiationException e) {
            Engine.LogException(e);
        } catch (IllegalAccessException e) {
            Engine.LogException(e);
        }
    }

    public int getAllocated(Class c) {
        int total = 0;
        //for(Field field: c.getFields())
        //{
        //field.getGenericType();
        //}
        return total;
    }
}
