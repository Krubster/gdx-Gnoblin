package ru.alastar.debug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.alastar.game.components.GCamera;

/**
 * Created by mick on 06.06.15.
 */
public abstract class DebugRenderer {

    public abstract void render(Object c, ShapeRenderer to, GCamera by, SpriteBatch spriteBatch);

}
