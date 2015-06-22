package ru.alastar.graphics;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.utils.Array;
import ru.alastar.Engine;

import java.util.ArrayList;

/**
 * Created by mick on 16.05.15.
 * Copule of magic code!
 */
public class GEnvironment extends Environment {
    private ArrayList<BaseLight> _lights;

    public GEnvironment()
    {
        _lights = new ArrayList<BaseLight>();
    }

    @Override
    public Environment add (final BaseLight... lights) {
        for (final BaseLight light : lights)
            addLight(light);
        return this;
    }

    @Override
    public Environment add (final Array<BaseLight> lights) {
        for (final BaseLight light : lights)
            addLight(light);
        return this;
    }

    @Override
    public Environment add (DirectionalLight light) {
        addLight(light);
        return this;
    }

    @Override
    public Environment add (PointLight light) {
        addLight(light);
        return this;
    }

    @Override
    public Environment add (SpotLight light) {
        addLight(light);
        return this;
    }

    private void addLight(BaseLight l)
    {
        add(l);
    }

    @Override
    public Environment add(BaseLight l)
    {
        if(!_lights.contains(l))
            this._lights.add(l);
        else
            Engine.error("Can't add the same light twice!");
        return this;
    }

    public boolean removeLight(BaseLight light)
    {
        return  _lights.remove(light);
    }

    public ArrayList<? extends BaseLight> getLights() {
        return _lights;
    }
}
