package ru.alastar.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Created by mick on 16.05.15.
 */
public class SpriteRenderer extends BodyBridge {

    private Texture texture;
    private Matrix4 transform;
    private String texName = "";

    public SpriteRenderer()
    {
        super();
        transform = new Matrix4();
        texture = null;
    }

    public SpriteRenderer(Texture tes, String texName) {
        this();
        this.texture = tes;
        this.texName = texName;
    }

    @Override
    public Matrix4 getTransform() {
        return transform;
    }

    @Override
    public void setTransform(Vector3 pos, Vector3 scl, Quaternion rot) {
        transform.set(pos, rot, scl);
    }

    @Override
    public void recalculateBounds() {
    }

    @Override
    public Vector3 getCenter() {
        return null;
    }


    public Texture getTexture()
    {
        return texture;
    }

    @Override
    public float getRadius() {
        return texture.getWidth() >= texture.getHeight() ?  texture.getWidth() : texture.getHeight();
    }

    @Override
    public Vector3 getDims() {
        return null;
    }

    @Override
    public BoundingBox getBounds() {
        return null;
    }

    @Override
    public void setTransform(Matrix4 tr) {
        this.transform.set(tr);
    }

    public float getX() {
        Vector3 pos = new Vector3();
        transform.getTranslation(pos);
        return pos.x;
    }

    public float getY() {
        Vector3 pos = new Vector3();
        transform.getTranslation(pos);
        return pos.y;
    }

    public String getTexName() {
        return texName;
    }

    public void setTexName(String texName) {
        this.texName = texName;
    }
}
