package ru.alastar.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.GCamera;
import ru.alastar.utils.ScreenshotFactory;

import java.util.ArrayList;

/**
 * Created by mick on 04.05.15.
 */
public class GDirectionalLight extends Light {

    private long lastScreen = System.currentTimeMillis();

    protected float halfDepth;
    protected float halfHeight;

    @Override
    public void update (final Camera camera) {
        update(camera.position, tmpV.set(camera.direction).scl(halfHeight), camera.direction);
    }

    @Override
    public void act(float delta) {

    }

    public GDirectionalLight (int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight,
                                   float shadowNear, float shadowFar, Vector3 pos, Vector3 dir) {
        super(shadowMapWidth,shadowMapHeight,shadowViewportWidth,shadowViewportHeight,shadowNear,shadowFar, pos, dir);
        halfHeight = shadowViewportHeight * 0.5f;
        halfDepth = shadowNear + 0.5f * (shadowFar - shadowNear);

       // this.debug = true;
    }

    public GDirectionalLight set (final Color color){
        if (color != null) this.color.set(color);
        return this;
    }

    public GDirectionalLight set (final float r, final float g, final float b) {
        this.color.set(r, g, b, 1f);
        return this;
    }

    @Override
    public void update (final Vector3 pos, final Vector3 center, final Vector3 forward) {
        //cam.position.set(direction).scl(-halfDepth).add(center);
        //cam.direction.set(direction).nor();
        //cam.normalizeUp();
        //cam.position.set(pos.x, cam.position.y, pos.z);
        //final float halfFar = far / 2;
        //final float half

        //tmpV.set(pos);
        //tmpV2.set(forward);
        //tmpV2.scl(far / 2);
        //tmpV.add(tmpV2);
        //cam.position.set(tmpV);
        //cam.position.y = this.position.y;

        cam.position.set(direction).scl(-halfDepth).add(center);
        cam.direction.set(direction).nor();
        cam.normalizeUp();
        cam.update();
    }

    @Override
    public void begin (final Camera camera) {
        update(camera);
        begin();
    }

    @Override
    public void begin (final Vector3 center, final Vector3 forward) {
        //update(center, forward);
        begin();
    }

    @Override
    public void begin()
    {
        cam.update();//true false because i fuck everything, everybody and KATE dobrinina kek
       // final int w = fbo.getWidth();
       // final int h = fbo.getHeight();
        fbo.begin();
        Gdx.gl.glClearColor(1,1,1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void end () {
        if(System.currentTimeMillis() - lastScreen > 1000 && Engine.isDebug() && debug) {
            ScreenshotFactory.saveScreenshot(fbo.getWidth(), fbo.getHeight(), "depth_dir");
            lastScreen = System.currentTimeMillis();
        }
        fbo.end();
    }

    @Override
    public void renderShadow(ArrayList<GameObject> instances, GCamera related) {
        begin(related);

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