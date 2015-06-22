package ru.alastar.game.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import ru.alastar.Engine;
import ru.alastar.game.components.Rigidbody;
import ru.alastar.game.components.Rigidbody3D;

/**
 * Created by mick on 02.05.15.
 */
public class PhysicsContactListener extends ContactListener {
    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0,
                                  int userValue1, int partId1, int index1, boolean match1) {
        btCollisionObject obj1 = Engine.getWorld().getObject(userValue0);
        btCollisionObject obj2 = Engine.getWorld().getObject(userValue1);

        if (obj1 != null && obj2 != null) {
            Rigidbody r = Engine.getWorld().getRigidById(userValue0);
            Rigidbody r2 = Engine.getWorld().getRigidById(userValue1);

            if ((r != null && r2 != null)) {
                ((Rigidbody3D)r).getInfo().getOwner().onCollision(r2);
                ((Rigidbody3D)r2).getInfo().getOwner().onCollision(r);
            }
        }

        return true;
    }

    @Override
    public void onContactProcessed(int userValue0, int userValue1) {
    }
}
