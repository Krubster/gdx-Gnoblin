package ru.alastar.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.compression.Lzma;
import ru.alastar.Engine;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by mick on 23.05.15.
 */
public class ResourcePacker {

    public static int MUSIC_OFFSET;
    public static int TEXTURES_OFFSET;
    public static int MODELS_OFFSET;
    public static int FONTS_OFFSET;

    private static FileHandle _resources;
    private static File _file;
    private static File _tmpFile;
    public static boolean useLZMA = false;

    public static void load(String path)
    {
        _resources = FileManager.getLocalFileHandle(path);
        if(_resources != null)
        {
           _file = _resources.file();
            if(useLZMA)
            {
                InputStream decompress = FileManager.readFromFile(_file);
                _tmpFile = FileManager.createFile("tmp");
                OutputStream out = FileManager.writeInFile(_tmpFile);
                try {
                    Lzma.decompress(decompress, out);
                } catch (IOException e) {
                    Engine.logException(e);
                }
                _file = _tmpFile;
            }
            if(_file.exists()) {
                FileInputStream outStr = FileManager.readFromFile(_file);
                Engine.debug("Resource file size:" + _file.length());

                TEXTURES_OFFSET = FileManager.peekInt(outStr);
                MUSIC_OFFSET = FileManager.peekInt(outStr);
                MODELS_OFFSET = FileManager.peekInt(outStr);
                FONTS_OFFSET = FileManager.peekInt(outStr);

                //Reading order:
                //-Textures
                //-Music
                //-Models
                //-Fonts
                //Offset stores total size for each kind of assets

                int pos = 0;//byte pos;
                int format = 0;
                int dataSize = 0;//Up to 2 GBs
                byte[] data;

                FileHandle _tmpHandle = new FileHandle("_tmp");
                AssetDescriptor desc;
                File _texTmp;
                OutputStream tmpTexOut;

                while (pos < TEXTURES_OFFSET) {
                /*Texture saving order:
                - Format
                - Size of data
                - Data
                */

                    format = FileManager.peekInt(outStr);
                    dataSize = FileManager.peekInt(outStr);
                    pos += 8;

                    data = new byte[dataSize];
                    _texTmp = FileManager.createFile("_tmpTex");
                    tmpTexOut = FileManager.writeInFile(_texTmp);

                    for (int i = 0; i < dataSize; ++i) {
                        data[i] = FileManager.peekByte(outStr);
                        FileManager.putByte(tmpTexOut, data[i]);
                    }

                    _tmpHandle = new FileHandle(_texTmp);

                    Engine.debug("Resource texture. Format " + Pixmap.Format.values()[format].toString() + " dataSize: " + dataSize);
                    desc = new AssetDescriptor(_tmpHandle, Texture.class);
                    Engine.getAssetManager().load(desc);
                    Engine.getAssetManager().finishLoading();
                    pos += dataSize;
                }
            }
            else
            {
                Engine.error("Resources file not fount!");
                Engine.halt();
            }
        }
    }

    public static void pack(String to, boolean compress)
    {
        Array<Texture> arr = new Array<Texture>();
        Engine.getAssetManager().getAll(Texture.class, arr);
        int texture_offset = 0;
        int music_offset = 0;
        int models_offset = 0;
        int fonts_offset = 0;

        for(final Texture a: arr)
        {
            texture_offset += 8;
            texture_offset += a.getWidth() * a.getHeight() * 4;
        }

        Engine.debug("Textures offset: " + texture_offset);
        Engine.debug("Music offset: " + music_offset);
        Engine.debug("Models offset: " + models_offset);
        Engine.debug("Fonts offset: " + fonts_offset);

        File res = FileManager.createFile(to);
        try {
            res.createNewFile();
        } catch (IOException e) {
            Engine.logException(e);
        }

        OutputStream out = FileManager.writeInFile(res);
        FileManager.putInt(out, texture_offset);
        FileManager.putInt(out, music_offset);
        FileManager.putInt(out, models_offset);
        FileManager.putInt(out, fonts_offset);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            Engine.logException(e);
        }
        if(compress)
        {
            //TODO: do compression
        }
    }

    public static double bytesToDouble(byte firstByte, byte secondByte){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(firstByte);
        bb.put(secondByte);
        return (double)bb.getShort(0);
    }
}