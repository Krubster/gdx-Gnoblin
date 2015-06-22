package ru.alastar.game.components;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.GameObject;
import ru.alastar.utils.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by mick on 29.04.15.
 */
public class SoundListener extends BaseComponent {

    public ArrayList<SoundSource> sources = new ArrayList<SoundSource>();
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();
    private final Quaternion tmpQ = new Quaternion();

    public SoundListener() {
        super();
    }

    @Override
    public void process(float delta) {
        for (SoundSource s : sources) {
            if (s.emitter.is3D()) {
                s.emitter.getSound().setPan(s.soundId, Utils.getSoundPan(this.getOwner().getRotation(tmpQ), this.getOwner().getPosition(tmpV1), s.emitter.getOwner().getPosition(tmpV2)), Utils.getVolume(this, s.emitter));
                s.emitter.getSound().setPitch(s.soundId, s.emitter.getPitch());
                s.emitter.getSound().setVolume(s.soundId, Utils.getVolume(this, s.emitter));
            }
        }
    }

    public void addSource(SoundEmitter soundEmitter) {
        SoundSource s = new SoundSource(soundEmitter);
        if (!haveSource(soundEmitter)) {
            this.sources.add(s);
            s.soundId = soundEmitter.getSound().play(Utils.getVolume(this, soundEmitter), soundEmitter.getPitch(), Utils.getSoundPan(this.getOwner().getRotation(tmpQ), this.getOwner().getPosition(tmpV1), soundEmitter.getOwner().getPosition(tmpV2)));
            System.out.println("Adding source");
        }
    }

    public void removeSource(SoundEmitter soundEmitter) {
        if (haveSource(soundEmitter)) {
            for (SoundSource s : sources) {
                if (s.emitter.equals(soundEmitter)) {
                    sources.remove(s);
                    break;
                }
            }
        }
    }

    private boolean haveSource(SoundEmitter soundEmitter) {
        for (SoundSource s : sources) {
            if (s.emitter.equals(soundEmitter))
                return true;
        }
        return false;
    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);

    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }
}
