package ru.alastar.input;

import java.util.ArrayList;

public abstract class InputProcessor {

    private boolean waiting = false;
    private ArrayList<Integer> hold_keys;

    public InputProcessor(ArrayList<Integer> hold, int mc, String id, InputType t) {
        this.id = id;
        this.mainChar = mc;
        this.iType = t;
        hold_keys = new ArrayList<Integer>();
        hold_keys.addAll(hold);
    }

    //Main char that must be typed to invoke processor
    private int mainChar;

    private InputType iType;

    public abstract void act();

    public String id;

    public void processUp(int keycode) {
        //	EditorScreen.Log(LogLevel.FIRST, "[INPUT]", "In: " + keycode + " our: " + mainChar);
        if (keycode == mainChar) {
            if (iType.equals(InputType.UP)) {
                if (holding())
                    act();
            } else if (iType.equals(InputType.TYPE)) {
                if (!waiting)
                    waitForUp();
            }
        }
    }


    protected void waitForUp() {
        this.waiting = true;
    }

    public void processDown(int keycode) {
        //	EditorScreen.Log(LogLevel.FIRST, "[INPUT]", "In: " + keycode + " our: " + mainChar);
        if (keycode == mainChar) {
            if (iType.equals(InputType.DOWN)) {
                if (holding())
                    act();
            } else if (iType.equals(InputType.TYPE)) {
                if (waiting) {
                    waiting = false;
                    if (holding())
                        act();
                }
            }
        }
    }

    private boolean holding() {
        for (Integer i : hold_keys) {
            if (!InputAssociations.pressed(i)) {
                return false;
            }
        }
        return true;
    }

    public void processHold() {
        if(InputAssociations.pressed(mainChar) && iType.equals(InputType.HOLD))
        {
            act();
        }
    }
}
