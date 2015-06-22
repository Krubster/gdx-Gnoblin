package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import ru.alastar.game.GameObject;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 29.04.15.
 */
public interface Component {

    void onDeactivate();

    void onActivate();

    public void onChangeTransform(Matrix4 newTr, Matrix4 to);

    public void onCollision(Rigidbody r);

    public boolean getActive();

    public void setActive(boolean n);

    public abstract void process(float delta);

    public GameObject getOwner();

    public String getName();

    public void setName(String s);

    public void setOwner(GameObject owner);

    public abstract void deactivate();

    public abstract void activate();

    void dispose();

    public void saveTo(OutputStream out);

    public void loadFrom(InputStream in, GameObject go);

}
