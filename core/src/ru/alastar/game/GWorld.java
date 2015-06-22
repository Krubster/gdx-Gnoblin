package ru.alastar.game;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import ru.alastar.Engine;
import ru.alastar.game.components.Rigidbody;
import ru.alastar.game.components.Rigidbody3D;
import ru.alastar.game.components.SoundEmitter;
import ru.alastar.game.components.SoundListener;
import ru.alastar.game.physics.PhysicsContactListener;
import ru.alastar.log.LogLevel;

import java.util.ArrayList;
import java.util.Hashtable;

public class GWorld {


    // Collision filters
    public static final short STATIC_GEOMETRY  = 0x01;
    public static final short DYNAMIC_ENTITIES = 0x02;
    public static final short ALL_OBJECTS       = 0xFF;

    private PhysicsContactListener contactListener;
    private btSequentialImpulseConstraintSolver constraintSolver = null;
    public int version = 0;
    public int id = 0;
    public ArrayList<GameObject> instances = new ArrayList<GameObject>();
    public ArrayList<Decal> decals = new ArrayList<Decal>();

    //Bullet physics
    public btDynamicsWorld pWorld;
    private btBroadphaseInterface broadphase;
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    private Hashtable<Integer, btCollisionObject> rigids;

    //box2d physics
    private World p2dWorld;

    private static final Vector3 tmpV = new Vector3();
    private long lastStep;

    private static long physicsTickDelay = 10L;

    public GWorld(int id) {
        this.id = id;
        this.version = 0;
        if (Engine.useBulletPhysics()) {
            rigids = new Hashtable<Integer, btCollisionObject>();
            collisionConfig = new btDefaultCollisionConfiguration();
            dispatcher = new btCollisionDispatcher(collisionConfig);
            broadphase = new btDbvtBroadphase();
            constraintSolver = new btSequentialImpulseConstraintSolver();
            pWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
            pWorld.setGravity(Engine.getDefaultGravitation());
            ((btDiscreteDynamicsWorld) pWorld).setLatencyMotionStateInterpolation(false);
            contactListener = new PhysicsContactListener();
            Engine.debug("Bullet physics world initialized with tick delay: " + physicsTickDelay);
            lastStep = System.currentTimeMillis();
            if(Engine.debugDrawer != null)
            pWorld.setDebugDrawer(Engine.debugDrawer);
        }
        if(Engine.useBox2dPhysics())
        {
            p2dWorld = new World(new Vector2(Engine.getDefaultGravitation().x, Engine.getDefaultGravitation().y), true);
            Engine.debug("Box2d physics world initialized with tick delay: " + physicsTickDelay);
        }
    }

    public void addStatic(Rigidbody r)
    {
        if(r instanceof Rigidbody3D) {
            short colGroup = STATIC_GEOMETRY;
            short colMask = DYNAMIC_ENTITIES;
            pWorld.addRigidBody(((Rigidbody3D)r).getpBody(), colGroup, colMask);
            ((Rigidbody3D)r).getpBody().setFlags(btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
        }
        else
        {
            //TODO:Implement 2d static physics
        }
    }

    public void addDynamic(Rigidbody r)
    {
        if(r instanceof Rigidbody3D) {
            short colGroup = DYNAMIC_ENTITIES;
            short colMask = DYNAMIC_ENTITIES | STATIC_GEOMETRY;
            pWorld.addRigidBody(((Rigidbody3D) r).getpBody(), colGroup, colMask);
            ((Rigidbody3D)r).getpBody().setFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        }
        else
        {
            //TODO:Implement 2d dynamics physics
        }
    }

    public void stepPhysicSimulation(float delta)
    {
        if (Engine.canDoPhysics() && System.currentTimeMillis() - lastStep >= physicsTickDelay) {

            if (Engine.useBulletPhysics()) {
                final float d = Math.min(1f / 30f, delta);
                pWorld.stepSimulation(d, 5, 1f / 60f);
            }
            if (Engine.useBox2dPhysics()) {
                p2dWorld.step(Engine.getBox2dTimeStep(), Engine.getPrefVelocityIterations(), Engine.getPrefPositionIterations());
            }
        }
    }

    public btCollisionObject getObject(int id) {
        return rigids.get(id);
    }

    public void addObject(int i, btCollisionObject r) {
        rigids.put(i, r);
    }

    public int getFreeObjectId(int limit) {
        int id = Engine.random.nextInt(limit);
        if (rigids.containsKey(id))
            return getFreeObjectId(limit);
        else
            return id;
    }

    public void dispose() {
        if (Engine.useBulletPhysics()) {
            pWorld.dispose();
            broadphase.dispose();
            collisionConfig.dispose();
            dispatcher.dispose();
        }
        if(Engine.useBox2dPhysics())
            p2dWorld.dispose();
    }

    public void addGameObject(GameObject gameObject) {
        instances.add(gameObject);
    }

    public void deleteGO(GameObject gameObject) {
        for (GameObject go : instances) {
            if (go.equals(gameObject)) {
                instances.remove(go);
                break;
            }
        }
        gameObject.pool();
    }

    public void addDecal(Decal d) {
        this.decals.add(d);
    }

    public void removeDecal(Decal d) {
        if (decals.contains(d))
            decals.remove(d);
    }

    public void addSoundSource(SoundEmitter soundEmitter) {
        Engine.Log(LogLevel.FIRST, "SOUND", "Add sound source " + instances.size());

        for (GameObject go : instances) {
            Engine.Log(LogLevel.FIRST, "SOUND", "GO: " + go.tag);
            if (go.haveComponent(SoundListener.class)) {
                (go.getComponent(SoundListener.class)).addSource(soundEmitter);
            }
        }
    }

    public void removeSoundSource(SoundEmitter soundEmitter) {
        for (GameObject go : instances) {
            if (go.haveComponent(SoundListener.class)) {
                (go.getComponent(SoundListener.class)).removeSource(soundEmitter);
            }
        }
    }

    public Rigidbody getRigidById(int userValue0) {
        for (GameObject o : instances) {
            if (o.haveComponent(Rigidbody.class)) {
                return (o.getComponent(Rigidbody.class));
            }
        }
        return null;
    }

    public void removeGameObject(GameObject go) {
        if (instances.contains(go))
            this.instances.remove(go);
        else
            Engine.error("Can't remove GO that doesn't exist in world!");
    }

    public static void setPhysicsTickDelay(long physicsTickDelay) {
        GWorld.physicsTickDelay = physicsTickDelay;
    }

    public float getPhysicsTickDelay() {
        return physicsTickDelay;
    }


    public World getBox2dWorld() {
        return p2dWorld;
    }
}
