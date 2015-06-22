package ru.alastar.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;

/**
 * Created by mick on 18.05.15.
 */
public class Rigidbody2D extends Rigidbody implements Disposable {

    private BodyDef bodyDef;
    private Body body;
    private FixtureDef fixtureDef;
    private Fixture fixture;

    public Rigidbody2D(GameObject go, BodyDef.BodyType type, Shape shape)
    {
        this(type, go.getPosition2(new Vector2()), shape, 0.0f, 0.0f, 0.0f);
    }

    public Rigidbody2D(BodyDef.BodyType type, Vector2 pos, Shape shape, float restitution, float friction, float density)
    {
        super();
        bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(pos);

        body = Engine.getWorld().getBox2dWorld().createBody(bodyDef);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void setKinematic(boolean k)
    {

    }

    @Override
    public void applyForce(Vector3 direction) {

    }

    @Override
    public void setFriction(float v) {

    }

    @Override
    public void setWorldTransform(Matrix4 transform) {

    }

    @Override
    public void setAlwaysActive(boolean b) {

    }

    @Override
    public void clearForces() {

    }

    @Override
    public void dispose()
    {

    }
}
