package ru.alastar.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.GCamera;
import ru.alastar.utils.ScreenshotFactory;

import java.util.ArrayList;

/**
 * Created by mick on 07.06.15.
 */
public class GSpotLight extends Light {

    private long lastScreen = System.currentTimeMillis();
    public float angle = 25.0f;

    public GSpotLight(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight, float shadowNear, float shadowFar, Vector3 pos, Vector3 dir, float angle) {
        super(shadowMapWidth, shadowMapHeight, shadowViewportWidth, shadowViewportHeight, shadowNear, shadowFar, pos, dir);

        this.cam = new PerspectiveCamera(60, shadowViewportWidth,shadowViewportHeight);
        this.cam.position.set(pos);
        this.cam.far = shadowFar;
        this.cam.near = shadowNear;
        this.cam.direction.set(dir).nor();

        this.angle = angle;
        //this.debug = true;
    }

    @Override
    public void act(float delta) {

    }

    public GSpotLight set (final Color color){
        if (color != null) this.color.set(color);
        return this;
    }

    public GSpotLight set (final float r, final float g, final float b) {
        this.color.set(r, g, b, 1f);
        return this;
    }
    @Override
    public void begin (final Camera camera) {
        begin();
    }

    @Override
    public void begin (final Vector3 center, final Vector3 forward) {
        begin();
    }

    @Override
    public void begin()
    {
        cam.update();//true false because i fuck everything, everybody and KATE dobrinina kek
        //final int w = fbo.getWidth();
        //final int h = fbo.getHeight();
        fbo.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void end () {
        if(System.currentTimeMillis() - lastScreen > 1000 && Engine.isDebug() && debug) {
            ScreenshotFactory.saveScreenshot(fbo.getWidth(), fbo.getHeight(), "depth_spot");
            lastScreen = System.currentTimeMillis();
        }
        fbo.end();
    }

    @Override
    public void renderShadow(ArrayList<GameObject> instances, GCamera related) {
        begin();

        shaderProgram.begin();
        shaderProgram.setUniformf("u_cameraFar", getCamera().far);
        shaderProgram.setUniformf("u_lightPosition", getCamera().position);
        shaderProgram.end();

        shadowModelBatch.begin(getCamera());

        for (final GameObject go : instances) {
            if(go.getModel() != null && go.getActive() && go.castingShadows())
                shadowModelBatch.render(go.getModel());
        }

        shadowModelBatch.end();

        end();
    }
}
