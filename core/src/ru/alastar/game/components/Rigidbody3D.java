package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.game.physics.PShape;
import ru.alastar.game.physics.RigidInformation;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 19.05.15.
 */
public class Rigidbody3D extends Rigidbody {


    private int id = 0;
    private btRigidBody pBody;
    private RigidInformation info;

    public Rigidbody3D() {
    }

    public Rigidbody3D(GameObject to) {
        this(new RigidInformation(1.0f, PShape.SPHERE, to, false, false));
    }

    public Rigidbody3D(RigidInformation info) {
        super();
        build(info);
    }

    private void build(RigidInformation info) {
        if (Engine.useBulletPhysics()) {
            pBody = new btRigidBody(info.constructInfo());
            if (info.isKinematic()) {
                pBody.setCollisionFlags(pBody.getCollisionFlags()
                        | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
            }
            for (Integer flag : info.getCallbackFlags()) {
                pBody.setContactCallbackFlag(flag);
            }
            for (Integer filter : info.getCallbackFilters()) {
                pBody.setContactCallbackFilter(filter);
            }

            if (info.isNeverActivate())
                pBody.setActivationState(Collision.DISABLE_SIMULATION);
            if (info.isNeverDeactivate())
                pBody.setActivationState(Collision.DISABLE_DEACTIVATION);

            this.info = info;
            this.id = Engine.getWorld().getFreeObjectId(100000);
            this.pBody.setUserValue(id);
            this.pBody.setMotionState(info.getInfo().getMotionState());
            this.pBody.proceedToTransform(info.getOwner().getTransform());

            if(info.isStatic())
                Engine.getWorld().addStatic(this);
            else
                Engine.getWorld().addDynamic(this);

            Engine.getWorld().pWorld.addRigidBody(pBody);
            Engine.getWorld().addObject(id, pBody);
        }
    }

    @Override
    public void process(float delta) {

    }

    @Override
    public void onDeactivate() {
        if (pBody != null && pBody.isInWorld()) {
            Engine.getWorld().pWorld.removeCollisionObject(pBody);
            pBody.clearForces();
        }
    }

    @Override
    public void onActivate() {
        if (pBody != null && !pBody.isInWorld()) {
            Engine.getWorld().pWorld.addRigidBody(pBody);
        }
    }

    @Override
    public void setOwner(GameObject owner) {
        try {
            this.owner = owner;
            Engine.getWorld().pWorld.addRigidBody(pBody);
        } catch (NullPointerException e) {
            Engine.LogException(e);
        }
    }

    public void forceProceed(Matrix4 tr) {
        pBody.proceedToTransform(tr);
        pBody.setMotionState(info.getInfo().getMotionState());
        Engine.getWorld().pWorld.addRigidBody(pBody);

    }

    @Override
    public void dispose() {
        pBody.dispose();
        info.dispose();
    }

    public btRigidBody getpBody() {
        return pBody;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RigidInformation getInfo() {
        return info;
    }

    public void setMass(float mass) {
        this.info.setMass(mass);
        Vector3 v = new Vector3();
        this.pBody.getCollisionShape().calculateLocalInertia(mass, v);
        this.pBody.setMassProps(mass, v);
    }

    public void setKinematic(boolean kinematic) {
        if (kinematic)
            this.pBody.setCollisionFlags(pBody.getCollisionFlags()
                    | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        else
            pBody.setCollisionFlags(pBody.getCollisionFlags() ^ btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);

        this.getInfo().setKinematic(kinematic);

    }

    @Override
    public void applyForce(Vector3 direction) {
        this.pBody.applyCentralImpulse(direction);
    }

    @Override
    public void setFriction(float v) {
        pBody.setFriction(v);
    }

    @Override
    public void setWorldTransform(Matrix4 transform) {
        this.pBody.proceedToTransform(transform);
        pBody.activate();
    }

    @Override
    public void setAlwaysActive(boolean b) {
    }

    @Override
    public void clearForces() {
        pBody.clearForces();

    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);
        info.saveTo(out);
    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {
        RigidInformation info = new RigidInformation();
        info.loadFrom(in, go);
        build(info);
    }

    public void setShape(btCollisionShape shape) {
        if (pBody != null)
            this.pBody.setCollisionShape(shape);
    }

    public btCollisionShape getShape() {
        return pBody.getCollisionShape();
    }
}
