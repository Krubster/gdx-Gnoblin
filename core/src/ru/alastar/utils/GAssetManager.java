package ru.alastar.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Created by mick on 04.05.15.
 */
public class GAssetManager extends AssetManager {
    public GAssetManager() {
        super();
        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        this.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        this.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

    }

}
