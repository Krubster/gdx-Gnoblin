package ru.alastar.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


public abstract class ConstructedGUI {

    public abstract Actor getByName(String s);

    public abstract void register(Stage s);

    public abstract void show();

    public abstract void hide();


}
