package ru.alastar.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by mick on 07.06.15.
 */
public class GPointLight extends Light
{
    public float intensity = 1.0f;

    public GPointLight(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight, float shadowNear, float shadowFar, Vector3 pos, Vector3 dir) {
        super(shadowMapWidth, shadowMapHeight, shadowViewportWidth, shadowViewportHeight, shadowNear, shadowFar, pos, dir);
        this.cam = new PerspectiveCamera(100f, shadowViewportWidth,shadowViewportHeight);
        this.cam.position.set(pos);
        this.cam.far = shadowFar;
        this.cam.direction.set(dir).nor();
        this.cam.near = shadowNear;
    }

    @Override
    public void act(float delta) {

    }
    public GPointLight set (final Color color){
        if (color != null) this.color.set(color);
        return this;
    }

    public GPointLight set (final float r, final float g, final float b) {
        this.color.set(r, g, b, 1f);
        return this;
    }
}
