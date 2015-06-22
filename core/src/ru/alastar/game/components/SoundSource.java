package ru.alastar.game.components;

/**
 * Created by mick on 29.04.15.
 */
public class SoundSource {
    public SoundEmitter emitter;
    public long soundId;

    public SoundSource(SoundEmitter e) {
        this.emitter = e;
    }
}
