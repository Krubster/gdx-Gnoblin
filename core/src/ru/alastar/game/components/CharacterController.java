package ru.alastar.game.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.GameObject;
import ru.alastar.input.InputAssociations;
import ru.alastar.input.InputType;

import java.util.ArrayList;

/**
 * Created by mick on 17.05.15.
 */
public class CharacterController extends BaseComponent {

    final Vector3 tmpV1 = new Vector3();
    final Quaternion tmpQ = new Quaternion();
    public float speed = 0.5f;
    private GameObject go;
    public float mass = 1.0f;

    //TODO: add proper collision checking(with and without physics)
    public CharacterController(GameObject go)
    {
        super();
        this.go = go;
        ru.alastar.input.InputProcessor p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.W, "forward", InputType.HOLD){
            @Override
            public void act(){
               
                moveTo(1,0,0);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.S, "backward", InputType.HOLD){
            @Override
            public void act(){
                
                moveTo(-1,0,0);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.A, "strafeLeft", InputType.HOLD){
            @Override
            public void act(){
                
                moveTo(0,0,-1);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.D, "strafeRight", InputType.HOLD){
            @Override
            public void act(){
                
                moveTo(0,0,1);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.DPAD_LEFT, "turnLeft", InputType.HOLD){
            @Override
            public void act(){
                rotateTo(-1f, 0, 0);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.DPAD_RIGHT, "turnRight", InputType.HOLD){
            @Override
            public void act(){
                rotateTo(1f, 0, 0);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.DPAD_UP, "turnUp", InputType.HOLD){
            @Override
            public void act(){
                
                rotateTo(0,0,1f);
            }

        };
        InputAssociations.addProcessor(p);
        p = new  ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.DPAD_DOWN, "turnDown", InputType.HOLD){
            @Override
            public void act(){
                
                rotateTo(0,0,-1f);
            }

        };
        InputAssociations.addProcessor(p);
        if(go.haveComponent(Rigidbody.class))
        {
            go.getComponent(Rigidbody.class).setAlwaysActive(true);
            go.getComponent(Rigidbody.class).setFriction(0.0f);
        }
    }

    private void rotateTo(float i, float i1, float i2) {
        this.getOwner().rotateBy(new Vector3(i, i1, i2));
    }

    @Override
    public void process(float d)
    {
        //this.moveTo(Engine.getDefaultGravitation().x, Engine.getDefaultGravitation().y / 100, Engine.getDefaultGravitation().z);
    }


    private void moveTo(float i, float i1, float i2) {
        if(go.getTransform() != null) {
            tmpV1.set(i, i1, i2).scl(speed);
            go.getTransform().getRotation(tmpQ);

            //tmpV1.rotate(Vector3.X, tmpQ.getYaw());
            tmpV1.rotate(Vector3.Y, tmpQ.getYaw());
            tmpV1.rotate(Vector3.Z, tmpQ.getRoll());
            tmpV1.y = 0;

            //if(go.haveComponent(Rigidbody.class))
           // go.getComponent(Rigidbody.class).applyForce(tmpV1);
          //  Engine.debug(tmpV1.toString());
            go.addToPosition(tmpV1);
        }
    }
}
