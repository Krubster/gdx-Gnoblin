package ru.alastar.game.components;

import com.badlogic.gdx.audio.Sound;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.log.LogLevel;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 29.04.15.
 */
public class SoundEmitter extends BaseComponent {

    public float range = 1.0f;
    public float volume = 1.0f;
    public float pitch = 1.0f;
    public boolean is3D = false;
    private Sound sound;

    public SoundEmitter() {
        super();
    }

    public SoundEmitter(float r, Sound sound1, boolean is3D) {
        this.setRange(r);
        this.setSound(sound1);
        this.set3D(is3D);
    }

    public SoundEmitter Play() {
        Engine.getWorld().addSoundSource(this);
        Engine.Log(LogLevel.FIRST, "SOUND", "PLAY");
        return this;
    }

    public SoundEmitter Stop() {
        Engine.getWorld().removeSoundSource(this);
        return this;
    }

    @Override
    public void process(float delta) {

    }

    @Override
    public void deactivate() {
        super.deactivate();
        Stop();
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);

    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }
}
