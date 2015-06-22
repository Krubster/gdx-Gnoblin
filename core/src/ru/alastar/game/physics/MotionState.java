package ru.alastar.game.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import ru.alastar.game.GameObject;

/**
 * Created by mick on 02.05.15.
 */
public class MotionState extends btMotionState {

    public Matrix4 transform;
    public GameObject go;

    public MotionState(GameObject owner) {
        this.go = owner;
        this.transform = owner.getTransform();
    }

    public MotionState(Matrix4 tr) {
        this.transform = tr;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans)
    {
        if(go != null)
            worldTrans.set(go.getTransform());
        else
            worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        if(worldTrans != null) {
            if (go != null) {
                go.moveTransformToPRS(worldTrans);
                go.updateTransform();
                go.getTransform().set(worldTrans);
            } else {
                transform.set(worldTrans);
            }
        }
        else {
            this.getWorldTransform(worldTrans);
        }
    }
}
