package ru.alastar.lang;

public class Language {

    public String langName = "Generic Language";
    public EntryManager eManager;

    public Language(String n, EntryManager m) {
        langName = n;
        eManager = m;
    }

    public Entry getLangStringById(int strId) {
        return eManager.getStrById(strId);
    }

    public Entry getLangByString(String strName) {
        return eManager.getStrByIdStrName(strName);
    }

}
