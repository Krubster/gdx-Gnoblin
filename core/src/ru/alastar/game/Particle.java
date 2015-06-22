package ru.alastar.game;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.components.ParticleEmitter;
import ru.alastar.game.components.Rigidbody;
import ru.alastar.game.physics.PShape;
import ru.alastar.game.physics.RigidInformation;

/**
 * Created by mick on 29.04.15.
 */
public class Particle extends Decal {

    private float currentSpan = 0.0f;
    private static final Vector3 tmpV = new Vector3();
    private static final Quaternion tmpQ = new Quaternion();
    private Matrix4 transform;
    private Rigidbody body;

    public Particle() {
    }

    public Particle(boolean physics) {
        transform = new Matrix4();
        if (physics) {
            RigidInformation info = new RigidInformation(1.0f, PShape.BOX, transform, false, false);
        }
    }

    public Particle(Vector2 particlesSize) {
        this.setWidth(particlesSize.x);
        this.setHeight(particlesSize.y);
//        Engine.getEnvironment().world.addDecal(this);
    }

    public Particle startLifecycle(ParticleEmitter particleEmitter) {
        setCurrentSpan(0.0f);
        particleEmitter.reset(this);
        return this;
    }


    public float getCurrentSpan() {
        return currentSpan;
    }

    public void setCurrentSpan(float currentSpan) {
        this.currentSpan = currentSpan;
    }

    public void clear() {
        //  Engine.getEnvironment().world.removeDecal(this);
    }

    public void setFormPool(ParticleEmitter emitter, Vector2 particlesSize) {
        this.setPosition(emitter.getOwner().getPosition(tmpV));
        this.setRotation(emitter.getOwner().getRotation(tmpQ));
        this.setWidth(particlesSize.x);
        this.setHeight(particlesSize.y);
        this.setTextureRegion(emitter.getTexture());
        updateTransform();
        //      Engine.getEnvironment().world.addDecal(this);
    }

    private void updateTransform() {
        tmpV.set(scale.x, scale.y, 0);
        this.transform.set(position, rotation, tmpV);
    }
}
