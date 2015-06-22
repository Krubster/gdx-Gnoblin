package ru.alastar.game.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.Rigidbody3D;

/**
 * Created by mick on 02.05.15.
 */
public class PhysicsUtils {
    public static btCollisionShape getShapeFor(GameObject gameObject, PShape sh) {
        btCollisionShape shape = null;
        if (sh == PShape.CUBE) {
            shape = new btBoxShape(new Vector3().set(gameObject.getDims().x, gameObject.getDims().y, gameObject.getDims().z));
        } else if (sh == PShape.SPHERE) {
            shape = new btSphereShape(gameObject.getDims().len() / 2);
        } else if (sh == PShape.TERRAIN) {
            Array arr = new Array();
            arr.add(gameObject.getModel().nodes.first().parts.first().meshPart);
            shape = new btBvhTriangleMeshShape(arr);
        } else if (sh == PShape.BOX) {
            shape = new btBoxShape(gameObject.getDims());
        }
        return shape;
    }

    public static Rigidbody3D create3DRigidBody(GameObject gameObject, float v, PShape t, boolean b, boolean isstatic) {
        RigidInformation info = new RigidInformation(v, t, gameObject, b, isstatic);
        Rigidbody3D r = new Rigidbody3D(info);
        return r;
    }
}
