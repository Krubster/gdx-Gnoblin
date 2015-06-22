package ru.alastar.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;
import ru.alastar.game.components.*;
import ru.alastar.graphics.GEnvironment;
import ru.alastar.utils.FileManager;
import ru.alastar.utils.ObjectPooler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class GameObject {

    public static final int SAVE_FLAG = 1;
    public static final int ACTIVE = 2;
    public static final int IGNORE_FAR_CULLING = 4;
    public static final int CASTING_SHADOWS = 8;

    public String tag = "";

    //Fields duplicating our transform(Used for transformation manipulations)
    public Vector3 position = new Vector3();
    public Quaternion rotation = new Quaternion();
    public Vector3 scale = new Vector3();

    //Should I move it to the renderers classes?
    public GMaterial goMaterial = null;

    protected Matrix4 transform = null;
    private GameObject parent;
    private ArrayList<GameObject> children = new ArrayList<GameObject>();

    protected ArrayList<Component> components = new ArrayList<Component>();

    protected int flags = 0;
    private final Vector2 tmpV2 = new Vector2();
    private final Vector3 tmpV3 = new Vector3();
    private final Quaternion tmpQ = new Quaternion();

    public GameObject(Model model, String tag, String mn) {
        this(tag);
        if (model != null) {
            MeshRenderer mr = new MeshRenderer(new ModelInstance(model), mn);
            this.addComponent(mr);
        }
    }

    public GameObject(ModelInstance model, String tag, String mn) {
        this(tag);
        if (model != null) {
            MeshRenderer mr = new MeshRenderer(model, mn);
            this.addComponent(mr);
        }
    }


    public GameObject(Texture tes, String tag, String texName)
    {
        this(tag);
        if(tes != null)
        {
            SpriteRenderer sr = new SpriteRenderer(tes, texName);
            this.addComponent(sr);
        }
    }

    public GameObject() {

    }

    public void setSerializeIgnore(boolean ignore) {
        if (ignore)
            this.flags ^= SAVE_FLAG;
        else
            this.flags |= SAVE_FLAG;// misha hello katya dobrinina whore otvechau, nashi 11 govnoegi mamashi ih wluxi, pust sdohnut lol
    }

    public GameObject(String tag) {
        this.tag = tag;
        setActive(true);
        transform = new Matrix4();
        transform.set(new Vector3(0, 0, 0), new Quaternion(), new Vector3(1, 1, 1));
        this.position = getPosition(new Vector3());
        this.rotation = getRotation(new Quaternion());
        this.scale = getScale(new Vector3());

        Engine.getWorld().addGameObject(this);
    }

    public void setEnableFarCulling(boolean n)
    {
        if(n)
            this.flags ^= IGNORE_FAR_CULLING;
        else
            this.flags |= IGNORE_FAR_CULLING;
    }

    public boolean getIgnoreSerialize()
    {
        return (this.flags & SAVE_FLAG) != SAVE_FLAG;
    }

    public boolean getIgnoreFarCulling()
    {
        return (this.flags & IGNORE_FAR_CULLING) == IGNORE_FAR_CULLING;
    }

    //010 010 100
    // |   &   &
    //110 110 111
    // =   =   =
    //110 010 100
    public void updateTransform() {

        for (final GameObject go : children) {
            go.updateParentTransform(position, rotation, scale);
        }

        for (final ru.alastar.game.components.Component c : components) {
            c.onChangeTransform(this.getTransform(), new Matrix4(position, rotation, scale));
        }

        apply();
    }

    public boolean haveComponent(Class ofType)
    {
        for(Component c: this.components)
        {
            if(ofType.isInstance(c))
            {
                return true;
            }
        }
        return false;
    }

    protected void updateParentTransform(Vector3 pos, Quaternion rotation, Vector3 scale) {
        if (getTransform() != null) {
            getParent().getTransform().getTranslation(tmpV3);
            tmpV3.set(pos.x - tmpV3.x, pos.y - tmpV3.y, pos.z - tmpV3.z);
            this.addToPosition(tmpV3);

            getParent().getScale(tmpV3);
            tmpV3.set(scale.x - tmpV3.x, scale.y - tmpV3.y, scale.z - tmpV3.z);
            this.scale(tmpV3);

            //final Vector3 dif_we_parent = this.getTransform().getTranslation(new Vector3()).sub(this.getParent().getPosition(new Vector3()));

            //final Vector3 rotDif = new Vector3(rotation.getAngleAround(Vector3.X), rotation.getAngleAround(Vector3.Y), rotation.getAngleAround(Vector3.Z));

            //rotDif.sub(getParent().getRotation(new Vector3()));
           // Engine.debug("Rotation dif: " + rotDif.toString());

            //this.translate (new Vector3(-dif_we_parent.x, -dif_we_parent.y, -dif_we_parent.z));
          //  this.rotate(this.getRotation(new Vector3()).add(rotDif));
         //   this.rotateBy(rotDif);
           // final Vector3 rotatedPos = getPosition(new Vector3());
           // rotatedPos.rotate(Vector3.X, rotDif.x);
           // rotatedPos.rotate(Vector3.Y, rotDif.y);
           // rotatedPos.rotate(Vector3.Z, rotDif.z);
           // rotatedPos.set(getPosition(new Vector3()).sub(rotatedPos));
           // this.addToPosition(rotatedPos);
            //this.translate(dif_we_parent);
        }
    }

    public Vector3 getRotation(Vector3 now) {
        Quaternion q = new Quaternion();
        this.getRotation(q);
        return now.set(q.getAngleAround(Vector3.X), q.getAngleAround(Vector3.Y), q.getAngleAround(Vector3.Z));
    }

    public void setModel(ModelInstance m, String name)
    {
        if (this.haveComponent(MeshRenderer.class)){
            Matrix4 tr = this.getComponent(MeshRenderer.class).getTransform();
            this.getComponent(MeshRenderer.class).setModel(m, name);
            this.getComponent(MeshRenderer.class).setTransform(tr);
        }
    }

    public Matrix4 getTransform() {
        if(haveComponent(BodyBridge.class))
            return this.getComponent(BodyBridge.class).getTransform();
        else{
            //Engine.debug("null mesh or sprite renderer");
            return transform;
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.setOwner(this);
    }

    public void addChild(GameObject go) {
        go.setParent(this);
        if (!children.contains(go))
            this.children.add(go);
        else
            Engine.error("Can't add child to parent twice!");
    }

    public void removeChild(GameObject go) {
        if (children.contains(go)) {
            children.remove(go);
            go.setParent(null);
        } else
            Engine.error("Can't remove GO from non-parent GO");
    }

    public boolean isParentOf(GameObject gameObject) {
        return children.contains(gameObject);
    }

    public void saveTo(OutputStream out) throws IOException {
        if(!getIgnoreSerialize()) {
            FileManager.putInt(out, GOType.GameObject.ordinal());
            FileManager.putInt(out, flags);
            FileManager.putString(out, tag);

            Vector3 pos = new Vector3();
            this.getTransform().getTranslation(pos);
            Quaternion q = new Quaternion();
            this.getTransform().getRotation(q);

            FileManager.putFloat(out, pos.x);
            FileManager.putFloat(out, pos.y);
            FileManager.putFloat(out, pos.z);
            FileManager.putFloat(out, q.x);
            FileManager.putFloat(out, q.y);
            FileManager.putFloat(out, q.z);
            FileManager.putFloat(out, q.w);

            Vector3 scl = new Vector3();
            this.getTransform().getScale(scl);

            FileManager.putFloat(out, scl.x);

            FileManager.putFloat(out, scl.y);

            FileManager.putFloat(out, scl.z);

            SaveComponents(out);

            FileManager.putInt(out, children.size());

            for (GameObject go : children) {
                Engine.getWorld().removeGameObject(go);
                go.saveTo(out);
            }
        }
    }

    void SaveComponents(OutputStream out) {
        FileManager.putInt(out, components.size()); // amount of components

        for (final Component c : components) {
            c.saveTo(out);
        }
    }

    public void render(ModelBatch modelBatch, Camera cam,  GEnvironment env) {
        if(getActive()) {
            if (getComponent(MeshRenderer.class) != null && getComponent(MeshRenderer.class).getModel() != null) {
                if (GCamera.isVisible(cam, this)) {
                    modelBatch.render(getComponent(MeshRenderer.class).getModel(), env);
                }
            }
        }
    }

    public <T extends Component> T getComponent(Class<T> cl) {
        for (final Component c : components) {
            if(cl.isInstance(c))
                return (T) c;
        }
        return null;
    }

    public static GameObject readGO(FileInputStream in) {
        Quaternion q = new Quaternion();
        Vector3 v = new Vector3();
        Vector3 scl = new Vector3();
        GameObject g;
        int flags = FileManager.peekInt(in);
        String tag = FileManager.peekString(in);

        v.x = FileManager.peekFloat(in);
        v.y = FileManager.peekFloat(in);
        v.z = FileManager.peekFloat(in);
        q.x = FileManager.peekFloat(in);
        q.y = FileManager.peekFloat(in);
        q.z = FileManager.peekFloat(in);
        q.w = FileManager.peekFloat(in);
        scl.x = FileManager.peekFloat(in);
        scl.y = FileManager.peekFloat(in);
        scl.z = FileManager.peekFloat(in);

        g = new GameObject(tag);
        g.setFlags(flags);
        int amt = FileManager.peekInt(in); //components size
        for (int i = 0; i < amt; ++i) {
            g.addComponent(BaseComponent.buildComponent(in, g));
        }

        GameObject go;
        int childCount = FileManager.peekInt(in);
        for (int i = 0; i < childCount; ++i) {
            go = GameObject.readGO(in);
            g.addChild(go);
        }

        g.setRotation(q);
        g.setPosition(v);
        g.scale(scl);
        return g;
    }

    public void setRotation(Quaternion q) {
        this.rotation.set(q);
        updateTransform();
    }

    public Vector3 getPosition(Vector3 chain) {
        this.getTransform().getTranslation(chain);
        return chain;
    }

    public Vector3 getScale(Vector3 chain) {
        this.getTransform().getScale(chain);
        return chain;
    }

    public Quaternion getRotation(Quaternion rotation) {
        return this.getTransform().getRotation(rotation);
    }

    public void pool() {
        Engine.getWorld().removeGameObject(this);

        for (final Component c : this.components) {
            c.deactivate();
        }
        components.clear();
        for (final GameObject go : children) {
            go.dispose();
        }
        children.clear();

        setActive(false);
        tag = "";

        if (ObjectPooler.getPool(GameObject.class).getCollection().size >= ObjectPooler.getPool(GameObject.class).maxPooled)
            dispose();
        else
            ObjectPooler.freeObject(this.getClass(), this);

    }

    public void delete() {
        this.pool();
    }

    protected void dispose() {
        for (final Component c : this.components) {
            c.dispose();
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean getActive() {
        return (flags & ACTIVE) == ACTIVE;
    }

    public void setActive(boolean a) {
        if(a)
            flags |= ACTIVE;
        else
            flags ^= ACTIVE;
    }

    public void setFromPool() {
        for (final Component c : components) {
            c.activate();
        }
    }

    public ModelInstance getModel() {
        if(haveComponent(MeshRenderer.class))
        return getComponent(MeshRenderer.class).getModel() ;
        else return null;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void onCollision(Rigidbody r) {
        for (final Component c : components) {
            c.onCollision(r);
        }
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public GameObject getParent() {
        return parent;
    }

    public ArrayList<GameObject> getChildren() {
        return children;
    }

    public void processComponents() {
        for (int i = components.size() - 1; i >= 0; --i) {
            if (components.get(i).getActive())
                components.get(i).process(Gdx.graphics.getDeltaTime());
        }
    }

    public void renderShadow(ModelBatch shadowModelBatch, Environment e, GCamera camera, float delta) {
        if(getComponent(MeshRenderer.class) != null)
             if(getComponent(MeshRenderer.class).getModel() != null)
                     shadowModelBatch.render(this.getComponent(MeshRenderer.class).getModel() , e);
    }

    public Vector3 getCenter() {
        if(getComponent(BodyBridge.class) == null)
            return this.position;
        else
            return getComponent(BodyBridge.class).getCenter();
    }

    public float getRadius() {
        if(getComponent(BodyBridge.class) == null)
            return 0;
        else
            return getComponent(BodyBridge.class).getRadius();
    }

    public Vector3 getDims() {
        if(getComponent(BodyBridge.class) == null)
             return Vector3.Zero;
        else
           return  getComponent(BodyBridge.class).getDims();
    }

    public void render2D(SpriteBatch batch, GCamera camera) {
        if(haveComponent(SpriteRenderer.class))
        {
            final SpriteRenderer renderer = getComponent(SpriteRenderer.class);
          //  Engine.debug("have component");

            if(renderer.getTexture() != null)
            {
                if(camera.seeSprite(renderer.getTransform()))
                    batch.draw(renderer.getTexture(), renderer.getX(), renderer.getY(), renderer.getTexture().getWidth(), renderer.getTexture().getHeight());
            }
        }
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    //Setting new position
    public void setPosition(Vector3 newPos)
    {
        this.position.set(newPos);
        updateTransform();
    }


    private void apply() {
        //Engine.debug(tag + " " + rotation.getYaw() + " " + rotation.getPitch() + " " + rotation.getRoll());

        this.getTransform().set(rotation);
        this.getTransform().setTranslation(position);
        this.getTransform().scale(scale.x, scale.y, scale.z);

        if(haveComponent(Rigidbody.class)) {
            getComponent(Rigidbody.class).setWorldTransform(getTransform());
            getComponent(Rigidbody.class).clearForces();
        }
    }

    //Just moving go by given vector in global coords(Used by physics)
    public void addToPosition(Vector3 add)
    {
        position.add(add);
        updateTransform();
    }

    //Translating go using it's rotation
    public void translate(Vector3 dir)
    {
        rotation.transform(dir);
        this.position.add(dir);
        updateTransform();
    }

    //Scaling by new values
    public void scaleBy(Vector3 by)
    {
        this.scale.set(by);
        updateTransform();
    }

    //Adding to the current scale
    public void scale(Vector3 add)
    {
        this.scale.add(add);
        updateTransform();
    }

    //Sets current rotation
    public void setRotation(Vector3 vector3) {
        this.rotation.setEulerAngles(vector3.x, vector3.y, vector3.z);
        updateTransform();
    }

    public void rotateBy(Vector3 vector3) {
        tmpQ.setEulerAngles(vector3.x, vector3.y, vector3.z);
        rotation.mul(tmpQ);
        updateTransformRotation(vector3);
    }

    private void updateTransformRotation(Vector3 vector3) {
        for (final GameObject go : children) {
            go.updateRotation(vector3);
        }

        for (final ru.alastar.game.components.Component c : components) {
            c.onChangeTransform(this.getTransform(), new Matrix4(position, rotation, scale));
        }

        apply();
    }

    private void updateRotation(Vector3 rot) {
        //TODO: Implement children rotation for ever and ever!!!!!!!!11
      //  final Vector3 antiRot = new Vector3(-this.rotation.getYaw(), -this.rotation.getPitch(), -this.rotation.getRoll());
      //  tmpV3.set(position);
      //  tmpV3.sub(getParent().position);
      //  tmpV3.set(-tmpV3.x, -tmpV3.y, -tmpV3.z);
      //  tmpQ.transform(tmpV3);
      //  this.translate(tmpV3);
        this.rotateBy(rot);
      //  tmpV3.set(-tmpV3.x, -tmpV3.y, -tmpV3.z);
      //  this.translate(tmpV3);
    }

    public void moveTransformToPRS(Matrix4 worldTrans) {
        worldTrans.getTranslation(position);
        worldTrans.getRotation(rotation);
        worldTrans.getScale(scale);
    }

    public Vector2 getPosition2(Vector2 vector2) {
        getPosition(tmpV3);
        tmpV2.set(tmpV3.x, tmpV3.y);
        return tmpV2;
    }

    public boolean castingShadows() {
        return (flags & CASTING_SHADOWS) == CASTING_SHADOWS;
    }

    public void setCastingShadows(boolean a) {
        if(a)
            flags |= CASTING_SHADOWS;
        else
            flags ^= CASTING_SHADOWS;
    }

}
