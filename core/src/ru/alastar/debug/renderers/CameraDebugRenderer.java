package ru.alastar.debug.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.alastar.Engine;
import ru.alastar.debug.DebugRenderer;
import ru.alastar.game.components.GCamera;

/**
 * Created by mick on 06.06.15.
 */
public class CameraDebugRenderer extends DebugRenderer {
    final float depth = 0.5f;
    final float height = 0.5f;
    final float width = 0.5f;
    @Override
    public void render(Object c, ShapeRenderer to, GCamera by, SpriteBatch spriteBatch) {//
    // Engine.debug("Rendering camera");
        GCamera cam = (GCamera)c;
        to.setAutoShapeType(true);
        to.begin();
        to.setColor(Color.WHITE);
        to.box(cam.position.x - width / 2, cam.position.y - height / 2, cam.position.z - depth / 2, width, height, depth);
        to.flush();
        to.end();
    }
}
