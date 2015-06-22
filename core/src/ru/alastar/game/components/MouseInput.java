package ru.alastar.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by mick on 17.05.15.
 */
public class MouseInput extends ru.alastar.game.components.BaseComponent {

    public MouseInput()
    {
        super();
    }

    @Override
    public void process(float delta) {
        if(getOwner() != null)
        {
                final int deltaX = Gdx.input.getDeltaX();
                final int deltaY = Gdx.input.getDeltaY();

                if(getOwner() != null)
                    getOwner().rotateBy(new Vector3(-(float)deltaX * 360.0f / Gdx.graphics.getWidth(), 0, -(float)deltaY *  360.0f / Gdx.graphics.getHeight()));
        }
    }
}
