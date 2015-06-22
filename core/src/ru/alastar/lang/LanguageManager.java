package ru.alastar.lang;

import com.badlogic.gdx.files.FileHandle;
import ru.alastar.Engine;
import ru.alastar.log.LogLevel;
import ru.alastar.utils.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LanguageManager {

    public static Language lang = new Language("", new EntryManager(0));

    public static void init(String name) throws IOException {
        FileHandle[] files = Engine.getLocaleDir();

        //   System.out.println("files amt: " + files.length);

        EntryManager eM;

        File langFile;
        FileReader fr;
        BufferedReader br;

        String s = "";
        String allowedCharacters = "";
        String line = "";
        String fontName = "";

        final ArrayList<String> lines = new ArrayList<String>();

        for (int i = 0; i < files.length; ++i) {

            langFile = files[i].file();
            System.out.println("file name: " + FileManager.ripExtension(langFile.getName()) + " our: " + name);

            if (FileManager.ripExtension(langFile.getName()).equals(name) && langFile.getName().toCharArray()[langFile.getName().toCharArray().length - 1] != '~') {

                System.out.println("file name -  " + FileManager.ripExtension(langFile.getName()));

                fr = new FileReader(langFile);
                br = new BufferedReader(fr);

                while ((s = br.readLine()) != null) {
                    lines.add(s);
                }

                br.close();

                fr.close();

                // System.out.println("Allowed chars for " +
                // langFile.getName()
                // + " are " + allowedCharacters);
                eM = new EntryManager(lines.size());

                // /////////////////
                // LOADING ENTRIES//
                // /////////////////
                // System.out.println(lines.size());
                for (int j = 0; j < lines.size(); ++j) {
                    line = lines.get(j);
                    // System.out.println(line + " j = " + j);
                    try {
                        eM.addEntry(new Entry(line.split("=")[0].toLowerCase(), line
                                .split("=")[1]));
                    } catch (Exception e) {
                        Engine.LogException(e);
                    }
                }

                lang = new Language(langFile.getName(), eM);
                Engine.Log(LogLevel.FIRST, "[LANG]", lang.langName + " WAS LOADED! ");
                lines.clear();
            }
        }

    }

    public static Language getLang() {
        return lang;
    }

    public static String getLocalizedMessage(String str) {
        return lang.getLangByString(str.toLowerCase()).strValue;
    }

    public static String getLocalizedMessage(int str) {
        return lang.getLangStringById(str).strValue;
    }


}
