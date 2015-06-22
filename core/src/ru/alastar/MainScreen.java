package ru.alastar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.CharacterController;
import ru.alastar.game.components.GCamera;
import ru.alastar.game.components.MouseInput;
import ru.alastar.game.components.Rigidbody3D;
import ru.alastar.game.physics.PShape;
import ru.alastar.game.physics.PhysicsUtils;
import ru.alastar.graphics.GDirectionalLight;
import ru.alastar.graphics.GSpotLight;
import ru.alastar.input.InputAssociations;
import ru.alastar.input.InputType;
import ru.alastar.utils.GDebugger;

import java.util.ArrayList;

/**
 * Created by mick on 03.05.15.
 */
public class MainScreen implements Screen, InputProcessor {

    private CameraInputController cameraInputController;

    public MainScreen() {
        Gdx.input.setInputProcessor(this);
        Engine.setDebug(true);
        Engine.setUseBulletPhysics(true);
       // Engine.setUseBox2dPhysics(true);
        Engine.setPDelay(10);
        Engine.setDraw2D(false);
        Engine.setDraw3D(true);
        Engine.setDoPhysics(true);
        Engine.allowConsole();
        Engine.setFarCulling(true);
        Engine.set_clearColor(Color.TEAL);
        Engine.init();

        //EXAMPLE CODE 3D

        ModelBuilder modelBuilder = new ModelBuilder();
        Material mat = new Material();
        mat.set(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE));
    //    Pixmap white = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
    //    white.setColor(Color.WHITE);
     //   white.fill();
        mat.set(new TextureAttribute(TextureAttribute.Diffuse, Engine.getTexture("grass.jpeg")));

        Model model = modelBuilder.createBox(1.0f, 1.0f, 1.0f, GL20.GL_TRIANGLES, mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        final GCamera cam = new GCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        cam.far = 500f;
        cam.position.set(3, 14, 5);
        cam.lookAt(0,0,0);
        MouseInput mouse = new MouseInput();
        mouse.setActive(true);

        final GameObject cube = new GameObject(model, "cube", "cube");
        final GameObject child = new GameObject(model, "cube2", "cube2");
        child.setPosition(new Vector3(0,7,0));
        cube.setPosition(new Vector3(0,5,0));
        cube.getTransform().setToTranslation(0,5,0);

        cube.addChild(child);

       // Rigidbody3D rigid = new Rigidbody3D(cube);
        //rigid.setAlwaysActive(true);
        //cube.addComponent(mouse);
        //cube.addComponent(cam);
       // cube.addComponent(rigid);
        cube.addComponent(new CharacterController(cube));

        cube.setCastingShadows(true);
        child.setCastingShadows(false);

        model = modelBuilder.createBox(100.0f, 0.1f, 100.0f, GL20.GL_TRIANGLES, mat, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        InputAssociations.addProcessor(new ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.Q, "test", InputType.TYPE) {
            @Override
            public void act() {
                cube.setPosition(new Vector3(0, 5, 0));
            }
        });

        GameObject plane = new GameObject(model, "plane2", "cube2");
        Rigidbody3D r = PhysicsUtils.create3DRigidBody(plane, 0.0f, PShape.BOX, true, true);
        r.setKinematic(true);
        plane.setPosition(new Vector3(0, -0.7f, 0));
        plane.addComponent(r);
        Engine.getEnvironment().set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f,
                0.4f, 1f));
        cameraInputController = new CameraInputController(cam);

        Engine.registerInputProcessor(cameraInputController);
        Engine.getEnvironment().set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        //TODO: add shadow mapping options that will affect quality of the shadowing
        GDirectionalLight d = new GDirectionalLight( Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2, Gdx.graphics.getWidth()/16, Gdx.graphics.getHeight()/16, 0.01f, 300f, new Vector3(15,5,1), new Vector3(0,0,0)).set(new Color(0.8f, 0.8f, 0.8f, 1.0f));

        d.lookAt(new Vector3(0,0,0));

        GSpotLight sl = new GSpotLight( Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2, Gdx.graphics.getWidth()/16, Gdx.graphics.getHeight()/16, 0.01f, 300f, new Vector3(0,19,0), new Vector3(0,0,0), 25.0f).set(new Color(0.8f, 0.8f, 0.8f, 1.0f));
        sl.lookAt(new Vector3(0,0,0));

         Engine.getEnvironment().add(d);
        Engine.getEnvironment().add(sl);

        Engine.addDebugger(new GDebugger() {
            @Override
            public String process() {
                return "Camera dir" + cam.direction.toString();
            }
        });
        Engine.addDebugger(new GDebugger() {
            @Override
            public String process() {
                return "Camera pos" + cam.position.toString();
            }
        });

        //Terrain terra = new Terrain(10, 10, new Vector3(0, 0, 0));
        //END EXAMPLE CODE - Make this better ^^^^

        //EXAMPLE CODE 2D
       /* Pixmap white = new Pixmap(155, 155, Pixmap.Format.RGBA8888);
        white.setColor(Color.WHITE);
        white.fill();
        GameObject go = new GameObject(new Texture(white), "test", "test");
        Rigidbody2D rigidbody2D = new Rigidbody2D(go, BodyDef.BodyType.DynamicBody, new CircleShape());
        GCamera cam = new GCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        go.setPosition(new Vector3(50,0,0));
        */
        //EXAMPLE CODE END
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        //Don't touch this
        Engine.render(v);

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int i) {
        Engine.keyDown(i);
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        Engine.keyUp(i);
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        Engine.keyTyped(c);
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        Engine.touchDown(i, i1, i2, i3);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        Engine.touchUp(i, i1, i2, i3);
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        Engine.touchDragged(i, i1, i2);
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        Engine.mouseMoved(i, i1);
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        Engine.scrolled(i);
        return false;
    }
}
