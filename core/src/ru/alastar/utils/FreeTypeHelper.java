package ru.alastar.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ru.alastar.Engine;

/**
 * Created by mick on 04.05.15.
 */
public class FreeTypeHelper {
    public static FreeTypeFontGenerator.FreeTypeFontParameter loadParameters(FileHandle f, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.kerning = Boolean.parseBoolean(getForFile(f.name(), "kerning"));
        parameter.flip = Boolean.parseBoolean(getForFile(f.name(), "flip"));
        parameter.size = Integer.parseInt(getForFile(f.name(), "size"));
        parameter.genMipMaps = Boolean.parseBoolean(getForFile(f.name(), "genMipMaps"));
        parameter.borderStraight = Boolean.parseBoolean(getForFile(f.name(), "borderStraight"));
        parameter.borderWidth = Float.parseFloat(getForFile(f.name(), "borderWidth"));
        parameter.characters = getForFile(f.name(), "characters");
        parameter.incremental = Boolean.parseBoolean(getForFile(f.name(), "incremental"));
        parameter.shadowColor = ParseColor(getForFile(f.name(), "shadowColor"));
        parameter.shadowOffsetX = Integer.parseInt(getForFile(f.name(), "shadowOffsetX"));
        parameter.shadowOffsetY = Integer.parseInt(getForFile(f.name(), "shadowOffsetY"));
        parameter.color = ParseColor(getForFile(f.name(), "color"));
        parameter.borderColor = ParseColor(getForFile(f.name(), "borderColor"));
        return parameter;
    }

    private static Color ParseColor(String colString) {

        Color color = new Color();
        try {
            color.set(Float.parseFloat(colString.split(" ")[0]), Float.parseFloat(colString.split(" ")[1]), Float.parseFloat(colString.split(" ")[2]), Float.parseFloat(colString.split(" ")[3]));
        } catch (Exception e) {
            Engine.LogException(e);
        }
        return color;
    }

    private static String getForFile(String name, String var) {
        String s = "";
        FileHandle f = FileManager.getLocalFileHandle(Engine.getFontsInfoDir() + "/" + FileManager.ripExtension(name) + ".txt");
        String allVars = f.readString();
        int pos = allVars.indexOf(var);
        pos += var.length() + 1;
        s = allVars.substring(pos);
        s = s.substring(0, s.indexOf('\n'));
        //Engine.debug(s);
        return s;
    }
}
