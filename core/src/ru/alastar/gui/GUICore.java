package ru.alastar.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import ru.alastar.Engine;

import java.util.Hashtable;

public class GUICore {

    private static final Hashtable<String, ConstructedGUI> _guis = new Hashtable<String, ConstructedGUI>();
    private static final Hashtable<String, Skin> _skins = new Hashtable<String, Skin>();
    private static final String selectedSkin = "default";

    public static void CreateDefaultSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap = new Pixmap(25, 25, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("check", new Texture(pixmap));
        BitmapFont font = Engine.getFont("tahoma.ttf");


        skin.add("default", font);
        skin.add("font", font);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.disabledFontColor = Color.GRAY;
        textButtonStyle.fontColor = Color.WHITE;
        WindowStyle windowStyle = new WindowStyle();
        windowStyle.background = skin.newDrawable("white", Color.GRAY);
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.CYAN;

        skin.add("default", windowStyle);

        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyle.font = font;
        textFieldStyle.messageFont = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.messageFontColor = Color.YELLOW;
        textFieldStyle.selection = skin.newDrawable("white", Color.BLUE);
        textFieldStyle.cursor = skin.newDrawable("white", Color.GREEN);
        textFieldStyle.focusedFontColor = Color.CYAN;

        ListStyle listStyle = new ListStyle();
        listStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        listStyle.font = font;
        listStyle.fontColorSelected = Color.GREEN;
        listStyle.fontColorUnselected = Color.GRAY;
        listStyle.selection = skin.newDrawable("white", Color.BLACK);

        skin.add("default", listStyle);

        ScrollPaneStyle spStyle = new ScrollPaneStyle();
        spStyle.hScroll = skin.newDrawable("white", Color.DARK_GRAY);
        spStyle.hScrollKnob = skin.newDrawable("white", Color.WHITE);
        spStyle.vScroll = skin.newDrawable("white", Color.DARK_GRAY);
        spStyle.vScrollKnob = skin.newDrawable("white", Color.WHITE);
        spStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        spStyle.corner = skin.newDrawable("white", Color.GRAY);

        skin.add("default", spStyle);

        SelectBoxStyle sbStyle = new SelectBoxStyle();
        sbStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        sbStyle.font = font;
        sbStyle.fontColor = Color.WHITE;
        sbStyle.backgroundDisabled = skin.newDrawable("white", Color.BLACK);
        sbStyle.backgroundOpen = skin.newDrawable("white", Color.LIGHT_GRAY);
        sbStyle.backgroundOver = skin.newDrawable("white", Color.GRAY);
        sbStyle.disabledFontColor = Color.DARK_GRAY;
        sbStyle.listStyle = skin.get("default", ListStyle.class);
        sbStyle.scrollStyle = skin.get("default", ScrollPaneStyle.class);

        skin.add("default", sbStyle);

        skin.add("default", textFieldStyle);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        skin.add("default", textButtonStyle);
        skin.add("default", labelStyle);

        CheckBox.CheckBoxStyle cbS = new CheckBox.CheckBoxStyle();
        cbS.checkboxOff = skin.newDrawable("check", Color.WHITE);
        cbS.checkboxOn = skin.newDrawable("check", Color.BLACK);
        cbS.checkboxOver = skin.newDrawable("check", Color.DARK_GRAY);
        cbS.font = font;
        skin.add("default", cbS);

        Tree.TreeStyle ts = new Tree.TreeStyle();
        ts.minus = skin.newDrawable("check", Color.BLUE);
        ts.plus = skin.newDrawable("check", Color.WHITE);
        ts.over = skin.newDrawable("white", Color.GRAY);
        ts.selection = skin.newDrawable("white", Color.BLACK);
        ts.background = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default", ts);


        _skins.put("default", skin);
    }

    public static ConstructedGUI addGUI(String s, ConstructedGUI g) {
        _guis.put(s, g);
        return g;
    }

    public static ConstructedGUI getByName(String s) {
        return _guis.get(s);
    }

    public static Object getStyle(Class<?> type) {
        return _skins.get(selectedSkin).get(type);
    }

    public static Skin getSelectedSkin() {
        return _skins.get(selectedSkin);
    }

    public static BitmapFont getFont() {
        return getSelectedSkin().getFont("font");
    }
}
