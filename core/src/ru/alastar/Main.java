package ru.alastar;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game implements ApplicationListener {

    private String title = "Game";

    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width,
                Gdx.graphics.getDesktopDisplayMode().height, false);
        Gdx.graphics.setTitle(title);
        this.setScreen(new MainScreen());
    }
}
