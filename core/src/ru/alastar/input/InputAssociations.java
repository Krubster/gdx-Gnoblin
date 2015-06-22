package ru.alastar.input;

import java.util.ArrayList;

public class InputAssociations implements com.badlogic.gdx.InputProcessor {

    private static ArrayList<InputProcessor> _processors = new ArrayList<InputProcessor>();
    private static ArrayList<Integer> _pressed = new ArrayList<Integer>();

    public static void addProcessor(InputProcessor processor) {
        _processors.add(processor);
    }

    public static void update()
    {
        for(int i = _processors.size() - 1; i >= 0; --i)
        {
            _processors.get(i).processHold();
        }
    }

    public static void removeProcessor(String s) {
        for (int i = _processors.size() - 1; i >= 0; --i) {
            if (_processors.get(i).id.equals(s)) {
                _processors.remove(_processors.get(i));
                break;
            }
        }
    }

    public static boolean pressed(Integer i) {
        return _pressed.contains(i);
    }

    @Override
    public boolean keyDown(int keycode) {
        _pressed.add(keycode);
        for (int i = _processors.size() - 1; i >= 0; --i) {
            _processors.get(i).processDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (pressed(keycode))
            _pressed.remove(_pressed.indexOf(keycode));
        for (int i = _processors.size() - 1; i >= 0; --i) {
            _processors.get(i).processUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
