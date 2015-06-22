package ru.alastar.gui.elements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.alastar.gui.GUICore;

import java.util.Hashtable;

/**
 * Created by mick on 02.05.15.
 */
public class TabSwitcher extends Table {

    private Table currentTab;
    private Table buttonsTable;
    private ScrollPane buttonsScroller;
    private ScrollPane contentScroller;

    private Hashtable<String, Table> content;

    public TabSwitcher(int width, int height) {
        content = new Hashtable<String, Table>();

        buttonsTable = new Table();
        buttonsTable.pad(5);

        currentTab = new Table();

        buttonsScroller = new ScrollPane(buttonsTable);
        buttonsScroller.setSmoothScrolling(true);
        buttonsScroller.setFadeScrollBars(true);
        buttonsScroller.setScrollBarPositions(true, true);
        buttonsScroller.setScrollbarsOnTop(true);
        buttonsScroller.setOverscroll(true, false);
        buttonsScroller.setScrollingDisabled(false, true);

        contentScroller = new ScrollPane(currentTab);
        contentScroller.setSmoothScrolling(true);
        contentScroller.setFadeScrollBars(true);
        contentScroller.setScrollBarPositions(true, true);
        contentScroller.setScrollbarsOnTop(true);
        contentScroller.setOverscroll(true, false);
        contentScroller.setScrollingDisabled(false, true);

        this.add(buttonsScroller).fill();
        this.row();
        this.add(contentScroller).minSize(width, height).fill();
    }

    public Table addTab(Table t, final String name) {
        if (!content.containsKey(name)) {
            TextButton button = new TextButton(name, GUICore.getSelectedSkin());
            buttonsTable.add(button).minWidth(150).maxWidth(150);

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setActive(name);
                }
            });
            content.put(name, t);
            return t;
        } else
            return null;

    }

    public Table createTab(final String name) {
        if (!content.containsKey(name)) {
            Table table = new Table();
            TextButton button = new TextButton(name, GUICore.getSelectedSkin());
            buttonsTable.add(button).minWidth(150).maxWidth(150);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setActive(name);
                }
            });
            content.put(name, table);
            return table;
        } else
            return null;

    }

    public void setActive(String name) {
        currentTab.clear();
        currentTab.add(content.get(name));
    }
}
