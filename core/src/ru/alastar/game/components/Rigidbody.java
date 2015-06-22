package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by mick on 30.04.15.
 */
public abstract class Rigidbody extends BaseComponent {

    public Rigidbody()
    {super();}

    public abstract void setKinematic(boolean flag);

    public abstract void applyForce(Vector3 direction);

    public abstract void setFriction(float v);

    public abstract void setWorldTransform(Matrix4 transform);

    public abstract void setAlwaysActive(boolean b);

    public abstract void clearForces();
}
