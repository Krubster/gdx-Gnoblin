package ru.alastar.debug;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.alastar.game.GameObject;
import ru.alastar.game.components.GCamera;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by mick on 06.06.15.
 */
public class RenderDebugger {
    private static GameObject goTmp = null;
    private static Object tmpComp;
    private static BaseLight tmpLight;
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static Hashtable<Class, DebugRenderer> _renderers = new Hashtable<Class, DebugRenderer>();

    public static void render(SpriteBatch spriteBatch, GCamera by, ArrayList<GameObject> objs)
    {
        for(int i = objs.size() - 1; i >= 0; --i){
            goTmp = objs.get(i);
            if(goTmp != null)
            {
                for(int j = goTmp.getComponents().size() - 1; j >=0 ; --j)
                {
                    tmpComp = goTmp.getComponents().get(j);
                    invokeRenderer(tmpComp, by, spriteBatch);
                }
                renderGizmo(goTmp, by, spriteBatch);
            }
        }
    }

    public static void renderLights(SpriteBatch sb, GCamera by, ArrayList<BaseLight> lights) {
        for(int i = lights.size() - 1; i >= 0; --i){
            tmpLight = lights.get(i);
            if(tmpLight != null)
            {
                invokeRenderer(tmpLight, by, sb);
                renderGizmo(tmpLight, by, sb);
            }
        }
    }

    private static void renderGizmo(GameObject goTmp, GCamera by, SpriteBatch spriteBatch) {
        
    }

    private static void renderGizmo(BaseLight goTmp, GCamera by, SpriteBatch spriteBatch) {

    }

    private static void invokeRenderer(Object c, GCamera by, SpriteBatch spriteBatch) {
        if(hasRenderer(c.getClass()))
        {
            getRenderer(c.getClass()).render(c, shapeRenderer, by, spriteBatch);
        }
    }


    public static void addRenderer(Class<? extends Object> component, DebugRenderer renderer)
    {
        if(!hasRenderer(component))
        {
            _renderers.put(component, renderer);
        }
    }

    public static void removeRenderer(Class<? extends Object> aClass)
    {
        if(hasRenderer(aClass))
        {
            _renderers.remove(aClass);
        }
    }

    public static DebugRenderer getRenderer(Class<? extends Object> aClass) {
        return _renderers.get(aClass);
    }

    public static boolean hasRenderer(Class<? extends Object> aClass) {
        return _renderers.containsKey(aClass);
    }
}
