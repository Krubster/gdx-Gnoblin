package ru.alastar.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import ru.alastar.Engine;
import ru.alastar.log.LogLevel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by mick on 27.04.15.
 */
public class FileManager {
    private static File tempF;

    public static boolean absoluteFileExists(String path) {
        Engine.Log(LogLevel.THIRD, "File Manager", "File exists request. Path: " + path);

        if (new File(path).exists())
            return true;
        else
            return false;
    }

    public static boolean localFileExists(String path) {

        return absoluteFileExists(System.getProperty("user.dir") + path);
    }

    public static File createFile(String f) {
        tempF = new File(System.getProperty("user.dir") + "/" + f);
        try {
            Engine.Log(LogLevel.THIRD, "File Manager", "Create file: " + tempF.getAbsolutePath());

            return Files.createFile(Paths.get(tempF.getAbsolutePath())).toFile();
        } catch (Exception e) {
            Engine.logException(e);
        }
        return null;
    }

    public static void deleteFile(String f) {
        Engine.Log(LogLevel.THIRD, "File Manager", "Delete file: " + f);

        tempF = new File(f);
        if (tempF.exists())
            tempF.delete();
    }

    public static File createDir(String f) {
        Engine.Log(LogLevel.THIRD, "File Manager", "Create directory: " + f);

        tempF = new File(System.getProperty("user.dir") + "/" + f);
        if (!tempF.exists())
            try {
                Files.createDirectory(tempF.toPath());
            } catch (Exception e) {
                Engine.logException(e);
            }
        return tempF;
    }

    public static String getExtension(File selectedFile) {
        return selectedFile.getPath().substring(selectedFile.getPath().lastIndexOf('.'));
    }

    public static BufferedOutputStream writeInFile(File f) {
        try {
            return new BufferedOutputStream(new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            Engine.logException(e);
        }
        return null;
    }

    public static BufferedOutputStream writeInFile(String f) {
        try {
            return new BufferedOutputStream(new FileOutputStream(f));
        } catch (FileNotFoundException e) {
            Engine.logException(e);
        }
        return null;
    }

    public static FileInputStream readFromFile(File f) {
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            Engine.logException(e);
        }
        return null;
    }

    public static OutputStream putInt(OutputStream in, int val) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
        try {
            in.write(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }

    public static OutputStream putFloat(OutputStream in, float val) {
        byte[] bytes = ByteBuffer.allocate(4).putFloat(val).array();
        try {
            in.write(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }

    public static OutputStream putShort(OutputStream in, short val) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(val).array();
        try {
            in.write(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }

    public static OutputStream putByte(OutputStream in, byte val) {
        byte[] bytes = ByteBuffer.allocate(1).put(val).array();
        try {
            in.write(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }


    public static OutputStream putString(OutputStream in, String val) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(val.getBytes().length).array();
        try {
            in.write(bytes);
            in.write(val.getBytes());
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }

    public static OutputStream putBoolean(OutputStream out, boolean active) {
        byte[] bytes;
        if (active)
            bytes = ByteBuffer.allocate(4).putInt(1).array();
        else
            bytes = ByteBuffer.allocate(4).putInt(0).array();
        try {
            out.write(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return out;
    }

    public static BufferedWriter writeLine(BufferedWriter in, String val) {
        try {
            in.write(val);
            in.newLine();
            in.flush();
        } catch (Exception e) {
            Engine.logException(e);
        }
        return in;
    }


    public static FileInputStream readFromFile(String f) {
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            Engine.logException(e);
        }
        return null;
    }

    public static int peekInt(InputStream in) {
        byte[] bytes = new byte[4];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static boolean peekBoolean(InputStream in) {
        byte[] bytes = new byte[4];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        int s = ByteBuffer.wrap(bytes).getInt();
        if (s == 1)
            return true;
        else
            return false;
    }

    public static float peekFloat(InputStream in) {
        byte[] bytes = new byte[4];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static short peekShort(InputStream in) {
        byte[] bytes = new byte[2];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return ByteBuffer.wrap(bytes).getShort();
    }

    public static String peekString(InputStream in) {
        int length = peekInt(in);
        byte[] bytes = new byte[length];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return new String(bytes);
    }

    public static byte peekByte(InputStream in) {
        byte[] bytes = new byte[1];
        try {
            in.read(bytes);
        } catch (Exception e) {
            Engine.logException(e);
        }
        return ByteBuffer.wrap(bytes).get();
    }

    public static String ripExtension(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }

    public static File getLocalFile(String f) {
        return getFile(System.getProperty("user.dir") + f);
    }

    public static File getFile(String f) {
        tempF = new File(f);
        if (tempF.exists())
            return tempF;
        else return null;
    }

    public static BufferedWriter readFromTextFile(File file) throws Exception {
        return new BufferedWriter(new FileWriter(file));
    }

    public static FileHandle getLocalFileHandle(String name) {
        return Gdx.files.internal(Engine.getDirectory() + "/" + name);
    }
}
