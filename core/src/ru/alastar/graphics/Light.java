package ru.alastar.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.GCamera;
import ru.alastar.graphics.shaders.DepthShader;

import java.util.ArrayList;

/**
 * Created by mick on 05.05.15.
 */
public abstract class Light extends BaseLight implements Disposable{

    public static ShaderProgram shaderProgram = null;
    public static ModelBatch shadowModelBatch = null;

    public Vector3 position = new Vector3();
    public Vector3 direction = new Vector3();
    protected FrameBuffer fbo;
    public Camera cam;
    protected final Vector3 tmpV = new Vector3();
    public boolean debug = false;

    public abstract void act(float delta);

    public Light(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight, float shadowNear, float shadowFar, Vector3 pos, Vector3 dir) {

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, shadowMapWidth, shadowMapHeight, true);

        cam = new OrthographicCamera(shadowViewportWidth, shadowViewportHeight);
        cam.near = shadowNear;
        cam.far = shadowFar;
        cam.position.set(pos);
        cam.direction.set(dir);

        position.set(pos);
        direction.set(dir);

        shaderProgram = Engine.setupShader("depth2");
        shadowModelBatch = new ModelBatch(new DefaultShaderProvider()
        {
            @Override
            protected Shader createShader(final Renderable renderable)
            {
                return new DepthShader(renderable, shaderProgram);
            }
        });
    }

    public void update (final Camera camera) {
    }

    public void rotate(Quaternion by){
        this.cam.rotate(by);
        direction = getCamera().direction;
        cam.update();
    }

    public void lookAt(Vector3 at)
    {
        getCamera().lookAt(at);
        direction = getCamera().direction;
        cam.update();
    }

    public void update (final Vector3 pos, final Vector3 center, final Vector3 forward) {

    }

    public void begin (final Camera camera) {

    }

    public void begin (final Vector3 center, final Vector3 forward) {

    }

    public void begin()
    {

    }


    public void end () {
    }

    public Camera getCamera () {
        return cam;
    }

    public Matrix4 getProjView () {
        return cam.combined;
    }

    public Texture getDepthMap () {
        return fbo.getColorBufferTexture();
    }

    public void dispose() {

    }


    public void renderShadow(ArrayList<GameObject> instances, GCamera related) {

    }
}
