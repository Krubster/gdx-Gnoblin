package ru.alastar.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;
import ru.alastar.game.GameObject;
import ru.alastar.game.Particle;
import ru.alastar.utils.ObjectPooler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mick on 29.04.15.
 */
public class ParticleEmitter extends BaseComponent {

    //TODO: WIP
    public int maxParticles = 1;
    private ArrayList<Particle> parts;
    public float gravity = 1.0f;
    public Vector3 moveDirection = new Vector3(0, 1, 0);
    public boolean usePhysics = false;
    public float lifeSpan = 3.0f; // in secs
    public float speed = 1.0f;
    private Vector3 tmpV = new Vector3();
    public boolean emitting = false;
    private Timer lifeSpanTimer;
    private final Vector3 tmpV2 = new Vector3();
    private Vector2 particlesSize = new Vector2(10, 10);
    private TextureRegion texture;
    public float randomNoise = 4.4f;
    private int launched = 0;
    private Timer launchTimer;
    public float launchTime = 2f;
    private boolean launching = false;
    private int i;

    public ParticleEmitter() {
        this(1.0f, 5, 5, new Vector2(15, 15), new TextureRegion(new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888))));
    }


    public ParticleEmitter(float speed, int max, int ls, Vector2 s, TextureRegion region) {
        this.speed = speed;
        this.maxParticles = max;
        this.lifeSpan = ls;
        parts = new ArrayList<Particle>();
        this.particlesSize.set(s);
        tmpV.set(moveDirection).scl(speed);
        texture = region;
        launchTimer = new Timer();
        startLifeSpanTimer();
        setMaterial(region);

    }

    private void startLifeSpanTimer() {
        lifeSpanTimer = new Timer();
        lifeSpanTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = parts.size() - 1; i >= 0; --i) {
                    if (parts.get(i).getCurrentSpan() < lifeSpan)
                        parts.get(i).setCurrentSpan(parts.get(i).getCurrentSpan() + 1.0f);
                    else
                        parts.get(i).startLifecycle(ParticleEmitter.this);
                }
            }
        }, 0, 1000);
    }

    public void Start() {
        emitting = true;
        for (Particle p : parts)
            p.setPosition(this.getOwner().getPosition(tmpV2));

    }

    public void Stop() {
        emitting = false;
    }

    public void setMaterial(TextureRegion tex) {
        for (i = parts.size() - 1; i >= 0; --i) {
            parts.get(i).setTextureRegion(tex);
        }
    }


    @Override
    public void process(float delta) {
        if (this.getOwner() != null) {
            if (this.getOwner().getActive()) {
                if (emitting) {
                    emit();
                }
            }
        }
        if (launched < maxParticles) {
            launchParticle();
        }
    }

    @Override
    public String getName() {
        return "Particle Emitter";
    }

    @Override
    public void deactivate() {
        lifeSpanTimer.cancel();
        launchTimer.cancel();
        for (i = parts.size() - 1; i >= 0; --i) {
            poolParticle(parts.get(i));
        }
        launched = 0;
        parts.clear();
        launching = false;
    }

    @Override
    public void activate() {
        startLifeSpanTimer();
        launchTimer = new Timer();
    }

    @Override
    public void dispose() {

    }

    private void launchParticle() {
        if (!launching) {
            launching = true;
            ++launched;
            launchTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Particle p = null;

                    if (ObjectPooler.hasObjects(Particle.class)) {
                        p = (Particle) ObjectPooler.getFreeObject(Particle.class);
                        p.setFormPool(ParticleEmitter.this, particlesSize);
                        p.startLifecycle(ParticleEmitter.this);
                    } else {
                        p = new Particle(particlesSize).startLifecycle(ParticleEmitter.this);
                        p.setTextureRegion(getTexture());
                    }
                    parts.add(p);
                    launching = false;
                    this.cancel();
                }
            }, (long) (1000 * launchTime));
        }
    }

    private void emit() {
        if (!usePhysics) {
            for (i = parts.size() - 1; i >= 0; --i) {
                parts.get(i).translate(tmpV);
            }
        }
    }

    public void reset(Particle particle) {
        if (emitting && getOwner() != null && getOwner().getActive()) {
            if (launched > maxParticles && emitting) {
                poolParticle(particle);
            } else {
                particle.setPosition(addNoise(this.getOwner().getPosition(tmpV2)));
            }
        }
    }

    private void poolParticle(Particle particle) {
        particle.clear();
        ObjectPooler.freeObject(Particle.class, particle);
        parts.remove(particle);
    }

    private Vector3 addNoise(Vector3 position) {
        return position.add(Engine.random.nextInt(2) * randomNoise, Engine.random.nextInt(2) * randomNoise, Engine.random.nextInt(2) * randomNoise);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public void saveTo(OutputStream out) {
        super.saveTo(out);
    }

    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }
}
