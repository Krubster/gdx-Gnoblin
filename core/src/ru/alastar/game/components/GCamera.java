package ru.alastar.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.graphics.GEnvironment;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.badlogic.gdx.graphics.Pixmap.Format;

;

/**
 * Created by mick on 29.04.15.
 */
public class GCamera extends com.badlogic.gdx.graphics.Camera implements Disposable, ru.alastar.game.components.Component {

    private static final Vector3 tmpsV = new Vector3();
    FrameBuffer fbo;
    SpriteBatch fboBatch;

    private final Vector3 tmpV = new Vector3();
    private boolean _masked = false;
    private ArrayList<String> _tagsToRender;
    private boolean usePostEffects = false;
    private GameObject owner = null;
    private boolean render = true;
    private float fieldOfView;
    private boolean perspective = true;
    private final Vector3 tmp = new Vector3();
    public float zoom = 1;
    private String name = "Camera";

    public GCamera(float fieldOfViewY, float viewportWidth, float viewportHeight, boolean persp) {
        super();
        this.fieldOfView = fieldOfViewY;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.perspective = persp;
        _tagsToRender = new ArrayList<String>();

        setStandard();
    }

    private void setStandard() {
        setName("Camera");
        far = 100f;
        near = 0.1f;
        update(true);
        initializeFBO();
        //Adding this camera to render
        Engine.addCamera(this);
    }

    public void initializeFBO() {
        if (fbo != null) fbo.dispose();
        if (fboBatch != null) fboBatch.dispose();
        fboBatch = new SpriteBatch();
        fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        fbo.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public GCamera(boolean m, ArrayList<String> t, float fieldOfViewY, float viewportWidth, float viewportHeight, boolean persp) {
        super();

        this.fieldOfView = fieldOfViewY;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        _tagsToRender = new ArrayList<String>();
        this.perspective = persp;
        if (m) {
            this.setMasked(t);
        }
        setStandard();
    }

    public boolean getMasked() {
        return _masked;
    }

    public void setMasked() {
        _masked = true;
    }

    public void addTag(String t) {
        if (!renderingGO(t)) {
            _tagsToRender.add(t);
        }
    }

    public boolean renderingGO(GameObject go) {
        return renderingGO(go.tag);
    }

    private boolean renderingGO(String tag) {
        return _tagsToRender.contains(tag);
    }

    public void removeTag(String t) {
        if (renderingGO(t)) {
            _tagsToRender.remove(t);
        }
    }

    public void setMasked(ArrayList<String> tags) {
        this.setMasked();
        _tagsToRender.addAll(tags);
    }

    public boolean isVisible( final GameObject instance) {
        if (instance.getTransform() != null) {
            tmpV.set(0, 0, 0);
            instance.getTransform().getTranslation(tmpV);
            tmpV.add(instance.getCenter());
           // Engine.debug("Camera - radius: " + instance.getRadius());
            if(!instance.getIgnoreFarCulling())
            return this.frustum.sphereInFrustum(tmpV, instance.getRadius());
        }
        return false;
    }

    public static boolean isVisible(final Camera c, final GameObject instance) {
        if (instance.getTransform() != null) {
            tmpsV.set(0, 0, 0);
            instance.getTransform().getTranslation(tmpsV);
            tmpsV.add(instance.getCenter());
            if(!instance.getIgnoreFarCulling())
                return c.frustum.sphereInFrustum(tmpsV, instance.getRadius());
        }
        return false;
    }

    public void render(ModelBatch batch, GEnvironment e, final ArrayList<GameObject> gos)
    {
        if (render) {
            this.update(true);
            batch.begin(this);
            for (final GameObject go : gos) {
                if (getMasked()) {
                    if (_tagsToRender.contains(go.tag)) {
                        go.render(batch, this, e);
                    }
                } else {
                    go.render(batch, this, e);
                }
            }
            batch.end();
        }
    }


    private void applyPostEffects(FrameBuffer fbo) {
        //TODO:Post effects code
    }

    public void render2D(SpriteBatch batch, ArrayList<GameObject> gos) {
        if (render) {
            this.update(true);
            batch.begin();
            for (final GameObject go : gos) {
                if (getMasked()) {
                    if (_tagsToRender.contains(go.tag)) {
                        go.render2D(batch, this);
                    }
                } else {
                    go.render2D(batch, this);
                }
            }
            batch.end();
        }
    }

    public void setToOrtho (boolean yDown) {
        setToOrtho(yDown, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setToOrtho (boolean yDown, float viewportWidth, float viewportHeight) {
        if (yDown) {
            up.set(0, -1, 0);
            direction.set(0, 0, 1);
        } else {
            up.set(0, 1, 0);
            direction.set(0, 0, -1);
        }
        position.set(zoom * viewportWidth / 2.0f, zoom * viewportHeight / 2.0f, 0);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        update();
    }


    public void renderDecals(DecalBatch decalBatch, ArrayList<Decal> decals) {
        for (Decal d : decals) {
            decalBatch.add(d);
        }
        decalBatch.flush();

    }

    @Override
    public void onDeactivate() {

    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onChangeTransform(Matrix4 oldTr, Matrix4 to) {

        to.getTranslation(tmpV);
        this.position.set(tmpV);

        update(true);

    }

    @Override
    public void onCollision(Rigidbody r) {

    }

    @Override
    public boolean getActive() {
        return render;
    }

    @Override
    public void setActive(boolean n) {
        this.render = n;
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
        return name;
    }

    @Override
    public void setName(String s) {
        name = s;
    }

    @Override
    public void setOwner(GameObject owner) {
        this.owner = owner;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void dispose() {
        fbo.dispose();
        fboBatch.dispose();
    }

    @Override
    public void saveTo(OutputStream out) {

    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }

    public void setUsePost(boolean usePost) {
        if (usePost)
            initializeFBO();

        this.usePostEffects = usePost;
    }

    public boolean isUsePost() {
        return usePostEffects;
    }



    @Override
    public void update() {
        update(true);
    }

    @Override
    public void update(boolean updateFrustum) {
        if(perspective) {
            float aspect = viewportWidth / viewportHeight;
            projection.setToProjection(Math.abs(near), Math.abs(far), this.fieldOfView, aspect);
            view.setToLookAt(position, tmp.set(position).add(direction), up);
            combined.set(projection);
            Matrix4.mul(combined.val, view.val);

            if (updateFrustum) {
                invProjectionView.set(combined);
                Matrix4.inv(invProjectionView.val);
                frustum.update(invProjectionView);
            }
        }
        else
        {
            projection.setToOrtho(zoom * -viewportWidth / 2, zoom * (viewportWidth / 2), zoom * -(viewportHeight / 2), zoom
                    * viewportHeight / 2, near, far);
            view.setToLookAt(position, tmp.set(position).add(direction), up);
            combined.set(projection);
            Matrix4.mul(combined.val, view.val);

            if (updateFrustum) {
                invProjectionView.set(combined);
                Matrix4.inv(invProjectionView.val);
                frustum.update(invProjectionView);
            }
        }
    }

    public void forceRender2D(SpriteBatch batch, ArrayList<ModelInstance> instances) {
    }

    public boolean seeSprite(Matrix4 transform) {
        //TODO:Implement culling
        return true;
    }
}
