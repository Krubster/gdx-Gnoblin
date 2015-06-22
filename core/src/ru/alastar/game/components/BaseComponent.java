package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.log.LogLevel;
import ru.alastar.utils.FileManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 02.05.15.
 */
public class BaseComponent implements Component, Disposable {

    protected boolean active = true;
    protected String name = "genericComponent";
    protected GameObject owner = null;


    public BaseComponent() {
        setName(this.getClass().getName());
    }

    @Override
    public void onCollision(Rigidbody r) {

    }

    @Override
    public void onChangeTransform(Matrix4 newTr, Matrix4 to) {

    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public void setActive(boolean n) {
        this.active = n;
        if (n)
            onActivate();
        else
            onDeactivate();
    }

    @Override
    public void onDeactivate() {
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void process(float delta) {

    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) { name = s; }

    @Override
    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    @Override
    public void deactivate() {
        this.setActive(false);
    }

    @Override
    public void activate() {
        this.setActive(true);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void saveTo(OutputStream out) {
        //Engine.Log(LogLevel.FIRST, "SAVE", this.getClass().getCanonicalName());
        FileManager.putString(out, this.getClass().getCanonicalName());
        FileManager.putBoolean(out, active);
        FileManager.putString(out, name);
    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {
        //Engine.Log(LogLevel.FIRST, "LOAD", "BASE COMPONENT METHOD");
    }


    public static BaseComponent buildComponent(FileInputStream in, GameObject to) {
        BaseComponent c = null;
        try {
            c = (BaseComponent) ClassLoader.getSystemClassLoader().loadClass(FileManager.peekString(in)).newInstance();
            c.setActive(FileManager.peekBoolean(in));
            c.setName(FileManager.peekString(in));
           // Engine.Log(LogLevel.FIRST, "LOAD", c.getClass().getCanonicalName() + " active: " + c.getActive());
            c.loadFrom(in, to);
        } catch (ClassNotFoundException e) {
            Engine.LogException(e);
        } catch (InstantiationException e) {
            Engine.LogException(e);
        } catch (IllegalAccessException e) {
            Engine.LogException(e);
        }
        return c;
    }
}
