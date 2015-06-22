package ru.alastar.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.utils.FileManager;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 13.05.15.
 */
public class MeshRenderer extends BodyBridge {

    private ModelInstance model;
    private String name;
    private final Vector3 center = new Vector3();
    private final Vector3 dimensions = new Vector3();
    private float radius;
    private final BoundingBox bounds = new BoundingBox();
    private final Vector3 tmpV = new Vector3();
    private final Vector3 tmpV2 = new Vector3();
    private final Quaternion tmpQ = new Quaternion();


    public MeshRenderer()
    {
        super();
        model = null;

        name = "NULL";
    }

    @Override
    public Matrix4 getTransform()
    {
        return model.transform;
    }

    @Override
    public void setTransform(Vector3 pos, Vector3 scl, Quaternion rot) {
    }

    @Override
    public void recalculateBounds() {

        model.calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
    }

    @Override
    public Vector3 getCenter() {
        return center;
    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);
        FileManager.putString(out, getName());
    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {
        this.name = FileManager.peekString(in);
        this.model = new ModelInstance(Engine.getModel(name));
    }


    public MeshRenderer(ModelInstance m, String name)
    {
        this.model = m;
        this.name = name;
        recalculateBounds();
    }

    public ModelInstance getModel() {
        return model;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public Vector3 getDims() {
        return dimensions;
    }

    @Override
    public BoundingBox getBounds() {
        return bounds;
    }

    @Override
    public void setTransform(Matrix4 tr) {
        if(model != null)
          this.model.transform.set(tr);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setModel(ModelInstance m, String name) {
        this.model = m;
        this.name = name;
    }
}
