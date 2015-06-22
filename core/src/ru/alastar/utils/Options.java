package ru.alastar.utils;

import ru.alastar.Engine;
import ru.alastar.log.LogLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * Created by mick on 27.04.15.
 */
public class Options {


    private static Hashtable<String, Object> _options = new Hashtable<String, Object>();
    private static File optionsFile;
    private static String name = "opt.s";
    private static boolean initialized = false;

    public static void specifyOptionsFileName(String s) {
        if (!initialized)
            name = s;
        else
            Engine.Log("[Options]", "Can't change file name after initialization");
    }

    public static void init() {
        loadOptions();
    }

    private static void loadOptions() {
        try {
            if (fileExists()) {
                Engine.Log(LogLevel.FIRST, "Options", "Options file exists...");

                optionsFile = FileManager.getFile(name);
                FileInputStream in = FileManager.readFromFile(optionsFile);
                FileManager.peekInt(in);
                addOption("fontSize", FileManager.peekInt(in));
                addOption("language", FileManager.peekString(in));
                in.close();
                Engine.Log(LogLevel.FIRST, "Options", "Options have been loaded!");
            } else
                setupOptions();
        } catch (IOException e) {
            Engine.LogException(e);
        }
        initialized = true;
    }

    private static void setupOptions() {
        optionsFile = FileManager.createFile(name);
        try {
            Engine.Log(LogLevel.FIRST, "Options", "Options file not exists...");
            OutputStream out = FileManager.writeInFile(optionsFile);
            FileManager.putInt(out, 1); //options version
            FileManager.putInt(out, 19); // font size
            FileManager.putString(out, "ru"); // lang
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addOption("fontSize", 19);
        addOption("language", "ru");
    }

    private static void addOption(String name, Object val) {
        if (!_options.containsKey(name)) {
            _options.put(name, val);
            Engine.Log(LogLevel.THIRD, "OPTIONS", "Option " + name + " was created successfully with value as " + val.toString());
        }
    }

    private static boolean fileExists() {
        if (Engine.getFile(name).exists())
            return true;

        return false;
    }

    public static Object getOption(String language) {
        return _options.get(language);
    }

    public static void setOption(String o, Object var) {
        if (_options.containsKey(o)) {
            _options.remove(o);
            _options.put(o, var);
        }
    }

    public static void save() {
        try {
            optionsFile.delete();
            optionsFile = FileManager.createFile(name);
            OutputStream out = FileManager.writeInFile(optionsFile);
            FileManager.putInt(out, 1); //options version
            FileManager.putInt(out, (Integer) getOption("fontSize")); // font size
            FileManager.putString(out, (String) getOption("language")); // lang
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Engine.Log(LogLevel.FIRST, "OPTIONS", "Options was saved!");
    }
}
