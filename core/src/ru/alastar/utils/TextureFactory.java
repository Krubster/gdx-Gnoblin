package ru.alastar.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;

public class TextureFactory {

    private static final Rectangle tmpR1 = new Rectangle();

    public static Pixmap paint(Pixmap tex, float x, float z, float f,
                               int brushStrength, Pixmap texToDraw, int meshX, int meshY) {
        int resolution = tex.getHeight() / 32;

        int left = meshX * tex.getWidth();
        int bottom = meshY * tex.getHeight();
        int top = (meshY + 1) * tex.getHeight();
        int right = (meshX + 1) * tex.getWidth();

        float pixX = x * resolution;
        float pixY = z * resolution;

        //	EditorScreen.Log("Tex", "Left: " + left + " Bottom: " + bottom + " top: " + top + " right: " + right
        //			+ " PixX: " + pixX + " pixY: " + pixY + " radiusPix: " + f * resolution);
        //System.out.println(" iterations: " + ((right - left) * (top - bottom)));
        //mask = new Pixmap((int)(f * 2 * resolution), (int)(f * 2 * resolution), Format.RGBA8888);
        //mask.drawPixmap(tiled, (int)(-pixX - f * resolution), (int)(-pixY - f * resolution));
        //	tex.drawPixmap(mask, (int)(pixX - left - f * resolution), (int)(pixY - bottom - f * resolution));

        int i;
        for (i = left; i <= right; ++i) {
            int j;
            for (j = bottom; j <= top; ++j) {
                if (inRange(i, j, pixX, pixY, f * resolution)) {
                    //	EditorScreen.Log("", i + " " + x * resolution + " " + j + " " + z * resolution);
                    //	EditorScreen.Log("", "InRange: " + (i - left) + " " + (j - bottom) + " Left: " + left + " Bottom: " + bottom);
                    tex.drawPixel(i - left, j - bottom, texToDraw.getPixel(getInBounds(i, texToDraw.getWidth()), getInBounds(j, texToDraw.getHeight())));
                }
            }
        }
        return tex;
    }

    private static boolean inRange(int i, float j, float pixX, float pixY,
                                   float r) {
        return tmpR1.set(pixX - r, pixY - r, r * 2, r * 2).contains(i, j);
    }

    private static int getInBounds(int i, int width) {
        return i - (int) Math.floor(i / width) * width;
    }
}
