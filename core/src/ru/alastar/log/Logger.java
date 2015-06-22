package ru.alastar.log;

import ru.alastar.utils.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mick on 27.04.15.
 */
public class Logger {

    static BufferedWriter writer;
    static File logFile;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static void init() {
        if (checkLogsFolder()) {
            startLogger();
        } else {
            setupLog();
        }
    }

    private static void setupLog() {
        File dir = FileManager.createDir("/logs");
        startLogger();
    }

    private static void startLogger() {
        String timeLog = dateFormat
                .format(Calendar.getInstance().getTime());
        File logFile = FileManager.createFile("/logs/" + timeLog + ".txt");

        try {
            writer = FileManager.readFromTextFile(logFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkLogsFolder() {
        if (FileManager.localFileExists("/logs"))
            return true;
        else
            return false;
    }

    public static void writeInLog(String string, String string2) {
        if (writer != null)
            FileManager.writeLine(writer, "[" + dateFormat
                    .format(Calendar.getInstance().getTime()) + "]" + string + ":" + string2);
    }

    public static void LogException(Exception e) {
        writeInLog("ERROR", e.getMessage());
        for (StackTraceElement el : e.getStackTrace()) {
            writeInLog("", el.toString());
        }
    }
}
