package ru.alastar.gui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import ru.alastar.gui.GUICore;

/**
 * Created by mick on 01.05.15.
 */
public class MenuElement extends TextButton {

    public EventListener listener;
    public Table dropdownTable;
    public TopMenu root;
    public MenuElement sectionRoot;
    public int numOfChildren = 0;

    public MenuElement(String text, Skin skin, final TopMenu root) {
        super(text, skin);
        dropdownTable = new Table();
        this.root = root;
        dropdownTable.setBackground(GUICore.getSelectedSkin().newDrawable("white", Color.DARK_GRAY));
        dropdownTable.setVisible(false);
        dropdownTable.addListener(new ClickListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (!isOver(dropdownTable, x, y) && allSectionsClosed()) {
                    hide();
                }
            }
        });
        this.setClip(true);
        this.align(Align.left);
    }

    private boolean allSectionsClosed() {
        for (Actor a : dropdownTable.getChildren()) {
            if (a instanceof MenuElement) {
                if (((MenuElement) a).dropdownTable.isVisible()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setListener(ClickListener listen) {
        this.listener = listen;
        this.addListener(listener);
    }

    public void setButton() {
        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
    }

    public void setSection(final MenuElement r) {
        this.sectionRoot = r;
        this.listener = new ClickListener() {

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                //   EditorScreen.Log(LogLevel.FIRST, "SECTION", "ENTERED");
                if (!isDisabled()) {
                    calculatePos(r);

                    dropdownTable.setVisible(true);

                    r.setActive(MenuElement.this);
                }
            }
        };

        this.addListener(listener);
    }

    public void setRootElement() {
        this.listener = new ClickListener() {

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (!isDisabled()) {
                    if (numOfChildren > 0) {
                        calculatePos(root);
                        dropdownTable.setVisible(true);
                        root.setActive(MenuElement.this);
                    }
                }
            }

        };
        this.addListener(listener);
    }

    public MenuElement addDropdown(MenuElement el) {
        dropdownTable.add(el).left().fill();
        dropdownTable.pack();
        if (this.getStage() != null) {
            calculatePos(root);
            ;
        }
        ++numOfChildren;
        return el;
    }

    public void removeDropdown(String name) {
        for (Actor a : dropdownTable.getChildren()) {
            if (((MenuElement) a).getName().equals(name)) {
                dropdownTable.removeActor(a);
                dropdownTable.pack();
                --numOfChildren;
                if (this.getStage() != null) {
                    calculatePos(root);
                    ;
                }
                break;
            }
        }
    }

    public MenuElement createDropdown(String text, String name, EventListener listen) {
        MenuElement el = new MenuElement(text, GUICore.getSelectedSkin(), root);
        if (listen != null) {
            el.removeListener(el.listener);
            el.addListener(listen);
        }
        el.setName(name);
        dropdownTable.add(el).left().fill();
        dropdownTable.pack();
        ++numOfChildren;
        return el;
    }

    public void register(Stage s) {
        s.addActor(dropdownTable);
        for (Actor a : dropdownTable.getChildren()) {
            if (a instanceof MenuElement) {
                ((MenuElement) a).register(s);
            }
        }
    }

    public void hide() {
        this.dropdownTable.setVisible(false);
        for (Actor a : dropdownTable.getChildren()) {
            if (a instanceof MenuElement) {
                ((MenuElement) a).hide();
            }
        }
    }

    public void setActive(MenuElement menuElement) {
        for (Actor a : dropdownTable.getChildren()) {
            if (a instanceof MenuElement) {
                if (!((MenuElement) a).equals(menuElement)) {
                    ((MenuElement) a).hide();
                }
            }
        }
    }

    public void calculatePos(Actor parent) {
        Vector2 pos = new Vector2();
        if (parent instanceof TopMenu) {//We are the root element
            pos = new Vector2(parent.getX() + this.getX(Align.left), parent.getY() - this.getY() - dropdownTable.getHeight());
        } else // we are the section
        {
            if (((MenuElement) parent).dropdownTable.getX(Align.right) + this.dropdownTable.getWidth() <= Gdx.graphics.getWidth()) {
                pos = new Vector2(((MenuElement) parent).dropdownTable.getX(Align.right), this.getHeight() + this.getParent().localToStageCoordinates(new Vector2(this.getY(Align.top), 0)).y - dropdownTable.getHeight());
                //  EditorScreen.Log(LogLevel.FIRST, "[POSITION]", "RIGHT ALIGN");

            } else {
                //  EditorScreen.Log(LogLevel.FIRST, "[POSITION]", "LEFT ALIGN");

                pos = new Vector2(((MenuElement) parent).dropdownTable.getX(Align.left) - this.dropdownTable.getWidth(), this.getHeight() + this.getParent().localToStageCoordinates(new Vector2(this.getY(Align.top), 0)).y - dropdownTable.getHeight());
            }
        }
        //  EditorScreen.Log(LogLevel.FIRST, "[POSITION]", pos.toString());

        dropdownTable.setPosition(pos.x, pos.y);

    }
}
