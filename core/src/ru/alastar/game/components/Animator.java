package ru.alastar.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import ru.alastar.game.GameObject;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mick on 30.04.15.
 */
public class Animator extends BaseComponent {
    private AnimationController animationController;
    private AnimationController.AnimationListener animationListener;

    public Animator() {
        super();
    }

    public Animator(ModelInstance m) {
        this.animationController = new AnimationController(m);
        animationListener = new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {

            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {

            }
        };
    }

    @Override
    public void process(float delta) {
        if (animationController != null)
            animationController.update(delta);
    }

    public void playLoop(String aName, int c) {
        animationController.setAnimation(aName, c);

    }

    //TODO: what is the time transition?
    public void playQueued(String aName) {
        animationController.queue(aName, 0, 10.0f, animationListener, 1.0f);
    }

    public void play(String aName) {
        animationController.animate(aName, 0, animationListener, 1.0f);
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);
    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }

}
