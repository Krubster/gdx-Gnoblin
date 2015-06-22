package ru.alastar.game.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import ru.alastar.game.GameObject;
import ru.alastar.utils.FileManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by mick on 02.05.15.
 */
public class RigidInformation {

    private btRigidBody.btRigidBodyConstructionInfo info;
    private float mass = 1.0f;
    private boolean isKinematic = false;
    private btCollisionShape shape;
    private Vector3 inertia = new Vector3();
    private MotionState mState;
    private ArrayList<Integer> callbackFlags = new ArrayList<Integer>();
    private ArrayList<Integer> callbackFilters = new ArrayList<Integer>();
    private boolean neverActivate = false;
    private boolean neverDeactivate = false;
    private GameObject owner;
    private PShape shapeType;
    private boolean isStatic = false;

    public RigidInformation() {

    }

    public RigidInformation(float mass, PShape t, GameObject owner, boolean kinematic, boolean stat) {
        this.mass = mass;
        this.shapeType = t;
        this.shape = PhysicsUtils.getShapeFor(owner, t);
        this.mState = new MotionState(owner);
        shape.calculateLocalInertia(mass, inertia);
        this.isKinematic = kinematic;
        this.owner = owner;
        this.isStatic = stat;
    }

    public RigidInformation(float mass, PShape t, Matrix4 tr, boolean kinematic, boolean stat) {
        this.mass = mass;
        this.shape = PhysicsUtils.getShapeFor(owner, t);
        this.mState = new MotionState(tr);
        shape.calculateLocalInertia(mass, inertia);
        this.isKinematic = kinematic;
        this.shapeType = t;
        this.owner = null;
        this.isStatic = stat;
    }

    public void setCallbackFilters(ArrayList<Integer> c) {
        this.callbackFilters = c;
    }

    public void setCallbackFlags(ArrayList<Integer> c) {
        this.callbackFlags = c;
    }

    public void addCallbackFlag(Integer i) {
        this.callbackFlags.add(i);
    }

    public void addCallbackFilter(Integer i) {
        this.callbackFilters.add(i);
    }

    public btRigidBody.btRigidBodyConstructionInfo constructInfo() {
        info = new btRigidBody.btRigidBodyConstructionInfo(mass, mState, shape, inertia);
        return info;
    }

    public btRigidBody.btRigidBodyConstructionInfo getInfo() {
        return info;
    }

    public boolean isKinematic() {
        return isKinematic;
    }

    public void dispose() {
        this.info.dispose();
        this.shape.dispose();
    }

    public ArrayList<Integer> getCallbackFlags() {
        return callbackFlags;
    }

    public ArrayList<Integer> getCallbackFilters() {
        return callbackFilters;
    }

    public boolean isNeverActivate() {
        return neverActivate;
    }

    public void setNeverActivate(boolean neverActivate) {
        this.neverActivate = neverActivate;
    }

    public boolean isNeverDeactivate() {
        return neverDeactivate;
    }

    public void setNeverDeactivate(boolean neverDeactivate) {
        this.neverDeactivate = neverDeactivate;
    }

    public GameObject getOwner() {
        return owner;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if (getInfo() != null)
            this.getInfo().setMass(mass);
    }

    public void setKinematic(boolean kinematic) {
        this.isKinematic = kinematic;
    }

    public void saveTo(OutputStream out) {

        FileManager.putFloat(out, getMass());
        FileManager.putBoolean(out, isKinematic);
        FileManager.putBoolean(out, isNeverActivate());
        FileManager.putBoolean(out, isNeverDeactivate());

        FileManager.putFloat(out, inertia.x);
        FileManager.putFloat(out, inertia.y);
        FileManager.putFloat(out, inertia.z);

        FileManager.putInt(out, callbackFilters.size());
        for (Integer i : callbackFilters) {
            FileManager.putInt(out, i);

        }

        FileManager.putInt(out, callbackFlags.size());
        for (Integer i : callbackFlags) {
            FileManager.putInt(out, i);

        }

        FileManager.putInt(out, shapeType.ordinal());
    }

    public void loadFrom(InputStream in, GameObject go) {
        setMass(FileManager.peekFloat(in));
        setKinematic(FileManager.peekBoolean(in));
        setNeverActivate(FileManager.peekBoolean(in));
        setNeverDeactivate(FileManager.peekBoolean(in));
        setInertia(FileManager.peekFloat(in), FileManager.peekFloat(in), FileManager.peekFloat(in));
        int filters = FileManager.peekInt(in);
        for (int i = 0; i < filters; ++i) {
            callbackFilters.add(FileManager.peekInt(in));
        }

        int flags = FileManager.peekInt(in);
        for (int i = 0; i < flags; ++i) {
            callbackFlags.add(FileManager.peekInt(in));
        }
        this.shapeType = PShape.values()[FileManager.peekInt(in)];
        shape = PhysicsUtils.getShapeFor(go, shapeType);
        this.constructInfo();
    }

    private void setInertia(float v, float v1, float v2) {
        this.inertia.set(v, v1, v2);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
