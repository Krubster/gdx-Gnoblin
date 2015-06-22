package ru.alastar.game.components;

import com.badlogic.gdx.math.Vector3;
import ru.alastar.game.GameObject;

import java.io.InputStream;

/**
 * Created by mick on 30.04.15.
 */
public class Vector3ObjectManagerTest extends BaseComponent {

    public Vector3 vector = new Vector3();
    public String name = "test";
    public int x = 12;
    public float floatVar = 0.12f;

    public Vector3ObjectManagerTest() {
        super();
    }

    @Override
    public void process(float delta) { //System.out.println(vector.x);
    }


    @Override
    public void loadFrom(InputStream in, GameObject go) {

    }

}
