package ru.alastar.utils;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import ru.alastar.Engine;
import ru.alastar.gui.GUICore;
import ru.alastar.gui.constructed.ConsoleView;
import ru.alastar.log.LogLevel;
import ru.alastar.log.Logger;

import java.util.Hashtable;

public class Console {

    private static final Hashtable<String, CommandProcessor> _commands = new Hashtable<String, CommandProcessor>();

    public static void init() {
        _commands.put("test", new CommandProcessor() {

            @Override
            public void processCommand(String[] args) {
                Engine.Log(LogLevel.SECOND, "TEST", "TEST COMMAND");
            }
        });
        _commands.put("setvar", new CommandProcessor() {

            @Override
            public void processCommand(String[] args) {
                try {

                    Class<?> c = Class.forName(args[0]);
                    Object t = c.getClass();
                    java.lang.reflect.Field staticFinalField = t.getClass().getField(args[1]);
                    staticFinalField.setAccessible(true);
                    if (args[2].toLowerCase() == "true" || args[2].toLowerCase() == "false") {
                        staticFinalField.set(t, Boolean.getBoolean(args[2]));
                    } else if ((args[2]).toCharArray()[args[2].length() - 1] == '\'' && (args[2]).toCharArray()[0] == '\'') {
                        staticFinalField.set(t, args[2]);
                    } else if (args[2].contains("int:")) {
                        staticFinalField.set(t, Integer.parseInt(args[2].substring(4, args[2].length())));
                    } else if (args[2].contains("float:")) {
                        staticFinalField.set(t, Float.parseFloat(args[2].substring(6, args[2].length())));
                    } else if (args[2].contains("double:")) {
                        staticFinalField.set(t, Double.parseDouble(args[2].substring(7, args[2].length())));
                    } else if (args[2].contains("short:")) {
                        staticFinalField.set(t, Short.parseShort(args[2].substring(6, args[2].length())));
                    }
                } catch (Exception e) {
                    Engine.LogException(e);
                    Logger.LogException(e);
                }
            }
        });
    }

    public static void pushCommand(String text) {
        String log = ((TextArea) GUICore.getByName("ConsoleView").getByName("log")).getText();
        ((ConsoleView) GUICore.getByName("ConsoleView")).write(log + "\n[COMMAND]: " + text);
        String command = text.split(" ")[0];
        String[] args = new String[0];
        if ((text.length() > 0) && text.contains(" ")) {
            text = text.substring(command.length() + 1, text.length());
            args = text.split(" ");
        }
        if (_commands.containsKey(command.toLowerCase())) {
            _commands.get(command.toLowerCase()).processCommand(args);
        }
    }

}
