package ru.alastar.utils;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.components.SoundEmitter;
import ru.alastar.game.components.SoundListener;

/**
 * Created by mick on 29.04.15.
 */
public class Utils {
    private static final Vector3 tmpV1 = new Vector3(), tmpV2 = new Vector3(), tmpV3 = new Vector3();
    private Quaternion tmpQ = new Quaternion();

    public static float getVolume(SoundListener soundListener, SoundEmitter soundEmitter) {
        if (soundListener.getOwner().getPosition(tmpV1).dst(soundEmitter.getOwner().getPosition(tmpV2)) > soundEmitter.getRange()) {
            return 0;
        } else
            return soundEmitter.getVolume() * soundEmitter.getRange() / soundListener.getOwner().getPosition(tmpV1).dst(soundEmitter.getOwner().getPosition(tmpV2));
    }

    public static float getSoundPan(Quaternion rotation, Vector3 listener, Vector3 emitter) {


        tmpV1.set(emitter);
        tmpV1.sub(listener);
        tmpV1.nor();

        tmpV2.set(1, 0, 0);

        tmpV2.rotate(Vector3.X, rotation.getYaw());
        tmpV2.rotate(Vector3.Y, rotation.getPitch());
        tmpV2.rotate(Vector3.Z, rotation.getRoll());



        final float scalar = tmpV1.x * tmpV2.x + tmpV1.y * tmpV2.y + tmpV1.z * tmpV2.z;

        //Engine.debug("SCL: " + scalar);
        //There's a mistake in algorithm, so we just changing sign
        return -scalar;
    }
}
