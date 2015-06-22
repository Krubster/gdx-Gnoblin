package ru.alastar.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Created by mick on 06.05.15.
 */
public class CameraFrameBuffer extends FrameBuffer {

    public CameraFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
        super(format, width, height, hasDepth, hasStencil);
    }

}
