package ru.alastar.debug.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.alastar.debug.DebugRenderer;
import ru.alastar.game.components.GCamera;
import ru.alastar.graphics.GDirectionalLight;

/**
 * Created by Alastar on 20.06.2015.
 */
public class DirectionalLightDebugRenderer extends DebugRenderer {

    public final int radius = 1;

    @Override
    public void render(Object c, ShapeRenderer to, GCamera by, SpriteBatch spriteBatch) {
        //Engine.debug("Render light");
        GDirectionalLight light = (GDirectionalLight)c;
        to.setAutoShapeType(true);
        to.begin();
        to.setColor(Color.BLUE);
        to.circle(light.position.x-radius, light.position.y-radius, light.position.z - radius, radius);
        to.flush();
        to.end();
    }
}
