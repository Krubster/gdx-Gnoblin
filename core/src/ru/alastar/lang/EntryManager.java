package ru.alastar.lang;

public class EntryManager {

    public Entry[] entries;

    public EntryManager(int entryArrL) {
        entries = new Entry[entryArrL + 1];
    }

    public boolean addEntry(Entry e) {
        try {
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i] == null) {
                    // System.out.println(" entry assign " + e.strValue);
                    entries[i] = e;
                    return true;
                }

            }
            return false;
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public Entry getStrById(int strId) {
        if (strId >= entries.length)
            return new Entry("Invalid", "Invalid");
        return entries[strId];
    }

    public Entry getStrByIdStrName(String strName) {
        try {
            // System.out.println("get str " + strName);
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i] != null) {
                    if (strName.equals(entries[i].strName))
                        return entries[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Entry("Invalid", "Invalid");
    }

}
