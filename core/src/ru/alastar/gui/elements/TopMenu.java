package ru.alastar.gui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import ru.alastar.gui.GUICore;

/**
 * Created by mick on 01.05.15.
 */
public class TopMenu extends Window {

    public TopMenu(String title, Skin skin, String styleName) {
        super(title, skin, styleName);
        this.setWidth(Gdx.graphics.getWidth());
        this.setHeight(25);
        this.align(Align.left);
        this.pad(2);
        this.setBackground(GUICore.getSelectedSkin().newDrawable("white", Color.DARK_GRAY));
        this.setPosition(0, Gdx.graphics.getHeight() - this.getHeight());
    }

    public Cell addElement(MenuElement el) {
        return this.add(el);
    }

    public void removeElement(String name) {
        for (Actor el : this.getChildren()) {
            if (((MenuElement) el).getName().equals(name.toLowerCase())) {
                this.removeActor(el);
                break;
            }
        }
    }

    public MenuElement getElement(String name) {
        for (Actor el : this.getChildren()) {
            if (((MenuElement) el).getName().equals(name.toLowerCase())) {
                return (MenuElement) el;
            }
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void register(Stage s) {
        s.addActor(this);
        for (Actor el : this.getChildren()) {
            if (el instanceof MenuElement)
                ((MenuElement) el).register(s);
        }
    }

    public void setActive(MenuElement menuElement) {
        for (Actor a : this.getChildren()) {
            if (a instanceof MenuElement) {
                if (a.getName() != menuElement.getName()) {
                    ((MenuElement) a).hide();
                }
            }
        }
    }

    public void disable(String object) {
        for (Actor el : this.getChildren()) {
            if (el instanceof MenuElement) {

                if (el.getName().equals(object.toLowerCase())) {
                    ((MenuElement) el).setDisabled(true);
                    ((MenuElement) el).hide();
                    break;
                }
            }
        }
    }

    public void enable(String object) {
        for (Actor el : this.getChildren()) {
            if (el instanceof MenuElement) {
                if (el.getName().equals(object.toLowerCase())) {
                    //  EditorScreen.Log(LogLevel.FIRST, "[ENABLE]", "ENABLED");
                    ((MenuElement) el).setDisabled(false);
                    ((MenuElement) el).setSkin(GUICore.getSelectedSkin());
                    break;
                }
            }
        }
    }
}
