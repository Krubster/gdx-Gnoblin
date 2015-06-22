package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Created by mick on 13.05.15.
 */
public  abstract class BodyBridge extends BaseComponent {

    public abstract Matrix4 getTransform();

    public  abstract void setTransform(Vector3 pos, Vector3 scl, Quaternion rot);

    public abstract void recalculateBounds();

    public abstract Vector3 getCenter();

    public abstract float getRadius();

    public abstract Vector3 getDims();

    public abstract BoundingBox getBounds();

    public abstract void setTransform(Matrix4 tr);
}
