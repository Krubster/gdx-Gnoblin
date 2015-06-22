package ru.alastar;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import ru.alastar.debug.DebugRenderer;
import ru.alastar.debug.RenderDebugger;
import ru.alastar.debug.renderers.CameraDebugRenderer;
import ru.alastar.debug.renderers.DirectionalLightDebugRenderer;
import ru.alastar.game.GOType;
import ru.alastar.game.GWorld;
import ru.alastar.game.GameObject;
import ru.alastar.game.Terrain;
import ru.alastar.game.components.GCamera;
import ru.alastar.graphics.GDirectionalLight;
import ru.alastar.graphics.GEnvironment;
import ru.alastar.graphics.Light;
import ru.alastar.graphics.shaders.DefaultShader;
import ru.alastar.gui.GUICore;
import ru.alastar.gui.constructed.ConsoleView;
import ru.alastar.input.InputAssociations;
import ru.alastar.input.InputType;
import ru.alastar.lang.LanguageManager;
import ru.alastar.log.LogLevel;
import ru.alastar.log.Logger;
import ru.alastar.utils.Console;
import ru.alastar.utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mick on 03.05.15.
 */
public class Engine {
/*

Changelog:
           Abandoned

    18.06.15:
     - Removed error from console while initializing engine
     - Some performance improvements

    15.06.15(v1.1.0):
     - Added shadow filtering
     - Added shadow casting flag
     - Fixed directional light fragment shadowing(YAY!)

    08.06.15(v1.0.0):
     - Finally, dealed with shadow mapping(Major up!)

    07.06.15(v0.5.19):
     - Finally, I dealed with depth map(rev up)!Yahooo

    05.06.15(v0.5.18):
     - Added event system(Qeque)

    24.05.15(v0.4.17):
     * Today was good hunting *
     - Engine can produce playable game. Minor up!
     - Object now rotating correctly(Horray!). Rev up!
       + Children now translating, scaling, rotating properly(For ever and ever)
     - Added passing projViewTrans matrix and depthMap to the shader. Rev up!

    21.05.15:
     - Get on well with physic objects
     - Character controller was fixed
     - Don't use kinematic bodies for cc - it sucks
     - Added 3d sound

    20.05.15:
     - Go's now can be safely affected by forced transformations, but this breaks rigidbody
     - Go's now can be safely assigned position, and this will not break rigid

    19.05.15:
     - GO's transformation moved to values dup
     - Physic GO's now can't be affected by forced transformations(Like translate, rotate etc.)
     - Added serialization ignoring to the gos
     - All boolean values switched to bit flags
     - Linked Rigidbody3d and 2d to rigidbody for bridge
     - WTF is happening to transform when assigning it in motionState?!

    18.05.15:
     - Rewrited GO's transformations(based on difs)
     - Fixed children scaling bug
     - Removed children rotation transformation
     - Added 2D go's rendering
     - Added 2D physics

    17.05.15:
     - Added MSAA
     - Added character controller and mouse input scripts(both WIP)
     - Added option HOLD for keys

    16.05.15:
     - Copied render code
     - Linked our GEnvironment to shader
     - Implemented 2d game objects rendering(Needs check)

    14.05.15:
     - Added switch physics option
     - Terrain indices switched to counter-clockwise
     - Terrain shadows looks very weird(BUG)
     - Only first chunk of terrain affected by shadows(BUG)

    13.05.15:
     - Added test shadow mapping(Better than nothing, but needs to be rewritten)
     - Moved model to component(MeshRenderer)

    08.05.15:
     - Fixed bug with physics transform updating(Changes didn't affect children)
     - Moved components processing to separate thread
     - Optimized some loops(Memory leaking was fixed)
     - Option for physics world ticking was added

    07.05.15:
     - Added children rotating relative to the parent
     - Updated to LibGdx 1.6.0

    06.05.15:
     - Added fancy texture drawing to terrain
     - Added GameObjects hierarchy
     - Added children scaling and translating relative to the parent

    04.05.15:
     - Input was compared in one InputMultiplexer
     - Added shader loader
     - Added freeType fonts loading
     - Some core changes
     - Added md5 crypt method
     - Added Network

	03.05.15:
	 - Added asset manager
	 - Separate of editor, engine project was created
	 - Added component saving
	 - Remove dynamic localization
	 - Fixed top menu
	 - Checked physics

Engine status: semi-normal
Engine TODO:
OTHER:
-Rewrite asset loading
-Sync somehow physic and forced transformations - done!
-Multiple worlds? - No
-Deal with depthmapping and light's camera - deal with it! yeah!
-Apply shadow - done!
-Add box2d natives
-New editor?-No
-Add point light, spot light
-Deal with resolution in light's camera - done!
2D:
-Physics:
  *Physics world - done!
  *Shapes - done!
  *Interaction
  *Water
-Render
  *GameObjects rendering - done!
  *Render optimization
  *2D animation
  *Atlases
  *Culling
-Graphics
  *2d lights
  *Water
3D;
-Render
  *Custom shaders loading
  *Optimize rendering thread
  *Should add renderableObject class for universal rendering - ??
  *Deal with shaders - done!
-GameObjects
  *Children rotation - done!
  *Children scaling - done!
  *Children translating - done!
-Terrain - done!
  *Building - done!
  *Terrain moving takes too much memory, solve it - solved
  *Editing - done!
-Terrain texturing - done!
  *Terrain brushes - almost done!
  *Terrain painting - done!
     +Painting algorithm - done!
     +Texture proper drawing - done!
     +Drawing effects as is fading, smoothing, brushes - done!
  *Terrain splitting - done!
-Physics - done!
  *Gravity - done!
  *Editor implemetation - done!
  *Physic interaction - done!
  *Physic cloth - in future,,,
  *Physic shapes - done! Need to add more shapes
-Graphics
  *Shaders
  *Shadows(Does it implemented in libgdx?)- done with implemented in libgdx. Rewrite in future
  *Water - need to learn about
  *Foliage - How? - meshes
  *PostEffects - How do I can implement this? - FrameBuffers/Shaders
  *Materials - will be soon, after the shadow mapping
  *Particles - can be rewritten - MUST be overwritten!
    +Emitter - done!
    +Physic emitter - what I must to emit?! D: (Decals is weird)
  *Animations - done!
    +Animator - done! Need check
-Sound - WIP
  *3D Sound - done!
  *Sound basics - done!
  *Sound effects - wut?
-Network - done!
-Mics
  *Input processor - done!
  *Localization manager - done!
  *File manager - done!
  *Hyper GUI
  *Resource packer/unpacker - do I need it?
    + Fast crypt/decrypt + LZMA packing
  *Camera filters - done!
  *Smart object creator
  *Logger - done!
  *Object pooler - done, do i need it? D: Maybe...
+++Optimization

BUGS:
 *Something causes memory leaks, fix it.
 *Terrain looks weird on the edges
 *Console View text field works incorrect - fixed(ANYWAY, WE NEED TO COPY TEXT FROM CONSOLE, SO WE CAN EDIT IT)
 *Strange errors from console - they disappeared
 *Something takes too much memory every frame - it's disappeared
 *Rewrite go's transformation code - done!
 *GO's transform changes when adding rigidbody - fixed
*/

    public static final int USE_BULLET = 1;
    public static final int USE_BOX2D = 2;
    public static final int DRAW_2D = 4;
    public static final int DRAW_3D = 8;
    public static final int DEBUG = 16;
    public static final int FAR_CULLING = 32;
    public static final int DO_PHYSICS = 64;
    public static final int PROCESS_COMPONENTS = 128;

    private static int FLAGS = 0;

    private static Stage stage;
    private static String directory = "";
    private static SpriteBatch batch;

    private static ModelBatch modelBatch;

    private static final String version = "1.1.0"; //major.minor.revision
    private static GWorld world;
    private static GAssetManager assetManager;
    private static ArrayList<GCamera> cameras = new ArrayList<GCamera>();
    private static GEnvironment environment;
    public static Random random = new Random();
    public static final int vertexSize = 9;
    private static BitmapFont standartFont;
    private static String texturesDir = "res/textures/";
    private static String modelsDir = "res/models/";
    private static String soundsDir = "res/sounds/";
    private static String musicDir = "res/music/";
    private static String fontFilesDir = "res/data/fonts/";
    private static String fontsInfoDir = "res/fonts/";
    private static String resourcesDir = "res/";
    private static String resourcesFileName = "resources";

    private static Color _clearColor = Color.BLACK;
    private static InputMultiplexer mul;
    private static int height;
    private static int width;
    private static long physicsTickDelay = 10L;

    private static final Vector3 _defaultGravitation = new Vector3(0, -10, 0);
    private static float box2dTimeStep = 1 / 60f;
    private static int prefVelocityIterations = 6;
    private static int prefPositionIterations = 2;


    public static DebugDrawer debugDrawer;
    private static ArrayList<GDebugger> _debuggers = new ArrayList<GDebugger>();

    public static void registerInputProcessor(InputProcessor p) {
        mul.addProcessor(p);
    }

    static int i;

    public static void removeProcessor(InputProcessor p) {
        try {
            mul.removeProcessor(p);
        } catch (Exception e) {
            LogException(e);
        }
    }

    public static void addDebugger(GDebugger d) {
        _debuggers.add(d);
    }

    public static boolean removeDebugger(GDebugger d) {
        return _debuggers.remove(d);
    }

    public static void init() {
        setDirectory(System.getProperty("user.dir"));
        Logger.init();
        Console.init();
        Options.init();

        if (useBulletPhysics()) {
            Bullet.init(true, true);
        }

        try {
            LanguageManager.init((String) Options.getOption("language"));
        } catch (IOException e) {
            LogException(e);
        }
        stage = new Stage();
        batch = new SpriteBatch();
        GWorld.setPhysicsTickDelay(getPDelay());
        world = new GWorld(1);

        environment = new GEnvironment();
        assetManager = new GAssetManager();

        if (isDebug()) {
            stage.setDebugAll(true);
            debugDrawer = new DebugDrawer();
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        }
        LoadResources();
        //ResourcePacker.load(getResourcesDir() + getResourcesFileName());

        final DefaultShader.Config c = new DefaultShader.Config();
        c.ignoreUnimplemented = true;
        modelBatch = new ModelBatch(new DefaultShaderProvider() {
            @Override
            protected Shader createShader(final Renderable renderable) {
                return new DefaultShader(renderable, c);
            }
        });

        GUICore.CreateDefaultSkin();
        mul = new InputMultiplexer();
        mul.addProcessor(stage);
        mul.addProcessor(new InputAssociations());

        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        GUICore.addGUI("ConsoleView", new ConsoleView()).register(stage);

        addDebugger(new GDebugger() {
            @Override
            public String process() {
                return "FPS: " + Gdx.graphics.getFramesPerSecond();
            }
        });
        addDebugger(new GDebugger() {
            @Override
            public String process() {
                return "JAVA HEAP: " + (Gdx.app.getJavaHeap() / 1024) + "/" + (Gdx.app.getJavaHeap() / 1024 / 1024) + "kB/MB";
            }
        });
        addDebugger(new GDebugger() {
            @Override
            public String process() {
                return "NATIVE HEAP " + (Gdx.app.getNativeHeap() / 1024) + "/" + (Gdx.app.getNativeHeap() / 1024 / 1024) + " Kb/MB";
            }
        });
        RenderDebugger.addRenderer(GCamera.class, new CameraDebugRenderer());
        RenderDebugger.addRenderer(GDirectionalLight.class, new DirectionalLightDebugRenderer());

    }

    public static ShaderProgram setupShader(final String prefix) {
        ShaderProgram.pedantic = false;

        final ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.classpath("ru/alastar/graphics/shaders/" + prefix + "_v.glsl"), Gdx.files.classpath("ru/alastar/graphics/shaders/" + prefix
                + "_f.glsl"));
        if (!shaderProgram.isCompiled()) {
            System.err.println("Error with shader " + prefix + ": " + shaderProgram.getLog());
            System.exit(1);
        } else {
            Gdx.app.log("init", "Shader " + prefix + " compilled " + shaderProgram.getLog());
        }
        return shaderProgram;
    }

    private static void LoadResources() {
        try {
            FileHandle[] textures = Gdx.files.internal(getTexturesDir()).list();
            FileHandle[] models = Gdx.files.internal(getModelsDir()).list();
            FileHandle[] sounds = Gdx.files.internal(getSoundsDir()).list();
            FileHandle[] music = Gdx.files.internal(getMusicDir()).list();
            FileHandle[] fonts = Gdx.files.internal(getFontFilesDir()).list();

            AssetDescriptor desc;
            TextureLoader.TextureParameter tpm;
            for (FileHandle f : textures) {
                debug("Loading texture: " + f.name());
                tpm = new TextureLoader.TextureParameter();
                tpm.genMipMaps = true;
                tpm.magFilter = Texture.TextureFilter.Linear;
                tpm.minFilter = Texture.TextureFilter.Linear;

                desc = new AssetDescriptor(f, Texture.class, tpm);
                assetManager.load(desc);
            }
            for (FileHandle f : models) {
                debug("Loading model: " + f.name());
                desc = new AssetDescriptor(f, Model.class);
                assetManager.load(desc);
            }
            for (FileHandle f : sounds) {
                debug("Loading sound: " + f.name());
                desc = new AssetDescriptor(f, Sound.class);
                assetManager.load(desc);
            }
            for (FileHandle f : music) {
                debug("Loading music: " + f.name());
                desc = new AssetDescriptor(f, Music.class);
                assetManager.load(desc);
            }
            FreetypeFontLoader.FreeTypeFontLoaderParameter freeTypeParams;
            FreeTypeFontGenerator.FreeTypeFontParameter parameter;
            for (FileHandle f : fonts) {
                debug("Loading font: " + f.name());
                if (f.name().contains(".ttf")) {
                    parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                    parameter = FreeTypeHelper.loadParameters(f, parameter);
                    freeTypeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                    freeTypeParams.fontParameters = parameter;
                    freeTypeParams.fontFileName = "res/data/fonts/" + f.name();
                    desc = new AssetDescriptor(f, BitmapFont.class, freeTypeParams);
                } else {
                    desc = new AssetDescriptor(f, BitmapFont.class);
                }
                assetManager.load(desc);
            }
            assetManager.finishLoading();
            debug(assetManager.getDiagnostics());
        } catch (Exception e) {
            LogException(e);
        }
        standartFont = getFont("tahoma.ttf");
        if (standartFont == null)
            debug("Standart font is null!");
    }

    //FIXME: Fix memory leaks
    public static void render(float delta) {
        try {
            Gdx.gl.glClearColor(_clearColor.r, _clearColor.b, _clearColor.g, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
            final ArrayList lights = environment.getLights();
            final int lightsSize = lights.size();
            for (i = 0; i < cameras.size(); ++i) {
                if (isDraw3D()) {
                    //Rendering depth maps TODO: Optimize
                    for (int j = lightsSize - 1; j >= 0; --j) {
                        if (lights.get(j) instanceof Light) {
                            ((Light) lights.get(j)).renderShadow(world.instances, cameras.get(i));
                        }
                    }

                    cameras.get(i).render(modelBatch, environment, world.instances);

                    if (isDebug())
                        RenderDebugger.renderLights(batch, cameras.get(i), lights);

                    if (isDebug())
                        RenderDebugger.render(batch, cameras.get(i), world.instances);
                }
                if (isDraw2D()) {
                    cameras.get(i).render2D(batch, world.instances);
                }
            }
            if (isDebug()) {
                batch.begin();
                for (int i = _debuggers.size() - 1; i >= 0; --i) {
                    GUICore.getFont().draw(batch, _debuggers.get(i).process(), 5, getHeight() - 6 - 20 * i);
                }
                batch.end();
            }

            stage.act();
            stage.draw();
            for (int i = world.instances.size() - 1; i >= 0; --i) {
                world.instances.get(i).processComponents();
            }
            InputAssociations.update();
            world.stepPhysicSimulation(delta);
        } catch (Exception e) {
            Logger.LogException(e);
        }
    }

    public static void allowConsole() {
        InputAssociations.addProcessor(new ru.alastar.input.InputProcessor(new ArrayList<Integer>(), Input.Keys.GRAVE, "console", InputType.TYPE) {
            @Override
            public void act() {
                ((ConsoleView) GUICore.getByName("ConsoleView")).switchView();
            }
        });
    }

    private static boolean isDraw2D() {
        return (FLAGS & DRAW_2D) == DRAW_2D;
    }

    private static boolean isDraw3D() {
        return (FLAGS & DRAW_3D) == DRAW_3D;
    }

    public static GWorld getWorld() {
        return world;
    }

    public static Model getModel(String mODEL_NAME) {
        return assetManager.get(getModelsDir() + mODEL_NAME, Model.class);
    }

    public static Stage getStage() {
        return stage;
    }

    public static boolean isDebug() {
        return (FLAGS & DEBUG) == DEBUG;
    }

    public static void setDebug(boolean debug) {
        if (debug)
            FLAGS |= DEBUG;
        else
            FLAGS ^= DEBUG;
    }

    public static GWorld loadWorld(String path) {
        GWorld w = new GWorld(0);
        try {
            FileInputStream in = FileManager.readFromFile(path);
            w.id = FileManager.peekInt(in);
            w.version = FileManager.peekInt(in);
            int instancesSize = FileManager.peekInt(in);

            GameObject g;
            GOType goType;
            int type;
            for (int i = 0; i < instancesSize; ++i) {
                type = FileManager.peekInt(in);
                goType = GOType.values()[type];
                if (goType.equals(GOType.GameObject)) {
                    g = GameObject.readGO(in);
                } else {
                    g = Terrain.readTerrain(in);
                }
                w.addGameObject(g);
            }
            if (in != null) {
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }

    public static void saveWorld(GWorld w, String n) {
        OutputStream out = FileManager.writeInFile(n);
        try {
            FileManager.putInt(out, w.id);
            FileManager.putInt(out, w.version);

            FileManager.putInt(out, w.instances.size());
            for (int i = world.instances.size(); i >= 0; --i) {
                world.instances.get(i).saveTo(out);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadScene(File selectedFile) {
        world = loadWorld(selectedFile.getAbsolutePath());
    }

    public static void SaveScene(File selectedFile) {
        saveWorld(world, selectedFile.getAbsolutePath());
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public static String getTexturesDir() {
        return texturesDir;
    }

    public static void setTexturesDir(String texturesDir) {
        Engine.texturesDir = texturesDir;
    }

    public static String getModelsDir() {
        return modelsDir;
    }

    public static void setModelsDir(String modelsDir) {
        Engine.modelsDir = modelsDir;
    }

    public static String getSoundsDir() {
        return soundsDir;
    }

    public static void setSoundsDir(String soundsDir) {
        Engine.soundsDir = soundsDir;
    }

    public static String getMusicDir() {
        return musicDir;
    }

    public static void setMusicDir(String musicDir) {
        Engine.musicDir = musicDir;
    }

    public static String getFontFilesDir() {
        return fontFilesDir;
    }

    public static void setFontFilesDir(String fontFilesDir) {
        Engine.fontFilesDir = fontFilesDir;
    }

    public static String getFontsInfoDir() {
        return fontsInfoDir;
    }

    public static void setFontsInfoDir(String fontsInfoDir) {
        Engine.fontsInfoDir = fontsInfoDir;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static long getPDelay() {
        return physicsTickDelay;
    }

    public static void setPDelay(long PDelay) {
        Engine.physicsTickDelay = PDelay;
    }

    public static boolean isFarCulling() {
        return (FLAGS & FAR_CULLING) == FAR_CULLING;
    }

    public static void setFarCulling(boolean farCulling) {
        if (farCulling) {
            FLAGS |= FAR_CULLING;
        } else {
            FLAGS ^= FAR_CULLING;
        }
    }

    public static boolean canDoPhysics() {
        return (FLAGS & DO_PHYSICS) == DO_PHYSICS;
    }

    public static void setDoPhysics(boolean canDoPhysics) {
        if (canDoPhysics)
            FLAGS |= DO_PHYSICS;
        else
            FLAGS ^= DO_PHYSICS;
    }

    public static float getBox2dTimeStep() {
        return box2dTimeStep;
    }

    public static void setBox2dTimeStep(float t) {
        box2dTimeStep = t;
    }

    public static int getPrefVelocityIterations() {
        return prefVelocityIterations;
    }

    public static void setPrefVelocityIterations(int prefPositionIterations) {
        Engine.prefVelocityIterations = prefPositionIterations;
    }

    public static int getPrefPositionIterations() {
        return prefPositionIterations;
    }

    public static void setPrefPositionIterations(int prefPositionIterations) {
        Engine.prefPositionIterations = prefPositionIterations;
    }

    public static void dispose() {
        batch.dispose();
        modelBatch.dispose();
        stage.dispose();
        world.dispose();
        assetManager.dispose();
    }

    public static void Log(String string, String string2) {
        Log(LogLevel.FIRST, string, string2);
    }

    public static void Log(LogLevel level, String string, String string2) {
        System.out.println(string + ":" + string2);
        if (level == LogLevel.FIRST || level == LogLevel.SECOND) {
            try {
                ConsoleView console = ((ConsoleView) GUICore.getByName("ConsoleView"));
                if (console != null)
                    console.write("\n" + string + ": " + string2);
            } catch (Exception e) {
                Logger.LogException(e);
            }
        }
        if (level == LogLevel.FIRST || level == LogLevel.THIRD) {
            Logger.writeInLog(string, string2);
        }
    }

    public static void LogException(Exception e) {
        Engine.Log(LogLevel.FIRST, "REFLECTION", e.toString());
        for (StackTraceElement el : e.getStackTrace()) {
            Engine.Log(LogLevel.FIRST, "", el.toString());
        }
    }

    public static Vector3 getDefaultGravitation() {
        return _defaultGravitation;
    }

    public static String getResourcesDir() {
        return resourcesDir;
    }

    public static void setResourcesDir(String resourcesDir) {
        Engine.resourcesDir = resourcesDir;
    }

    public static String getResourcesFileName() {
        return resourcesFileName;
    }

    public static void setResourcesFileName(String resourcesFileName) {
        Engine.resourcesFileName = resourcesFileName;
    }

    public static Color get_clearColor() {
        return _clearColor;
    }

    public static void set_clearColor(Color _clearColor) {
        Engine._clearColor = _clearColor;
    }

    public void set_defaultGravitation(Vector3 v) {
        this._defaultGravitation.set(v);
    }

    public static ModelBatch getModelBatch() {
        return modelBatch;
    }

    public static SpriteBatch getSpriteBatch() {
        return batch;
    }

    public static GEnvironment getEnvironment() {
        return environment;
    }

    public static boolean useBox2dPhysics() {
        return (FLAGS & USE_BOX2D) == USE_BOX2D;
    }

    public static void setUseBox2dPhysics(boolean usePhysics) {
        if (usePhysics)
            FLAGS |= USE_BOX2D;
        else
            FLAGS ^= USE_BOX2D;
    }


    public static boolean useBulletPhysics() {
        return (FLAGS & USE_BULLET) == USE_BULLET;
    }

    public static void setUseBulletPhysics(boolean usePhysics) {
        if (usePhysics)
            FLAGS |= USE_BULLET;
        else
            FLAGS ^= USE_BULLET;
    }

    public static File getFile(String name) {
        return new File(getDirectory() + "/" + name);
    }

    public static String getDirectory() {
        return directory;
    }

    private static void setDirectory(String directory) {
        Engine.directory = directory;
    }

    public static Texture getTexture(String texName) {
        return assetManager.get(getTexturesDir() + texName, Texture.class);
    }

    public static Sound getSound(String s) {
        return assetManager.get(getSoundsDir() + s, Sound.class);
    }

    public static Music getMusic(String s) {
        return assetManager.get(getMusicDir() + s, Music.class);
    }

    public static BitmapFont getFont(String s) {
        try {
            return assetManager.get(getFontFilesDir() + s, BitmapFont.class);
        } catch (Exception e) {
            return standartFont;
        }
    }

    public static void keyDown(int i) {
        mul.keyDown(i);
    }

    public static void keyUp(int i) {
        mul.keyUp(i);
    }

    public static void keyTyped(char c) {
        mul.keyTyped(c);
    }

    public static void touchDown(int i, int i1, int i2, int i3) {
        mul.touchDown(i, i1, i2, i3);
    }

    public static void scrolled(int i) {
        mul.scrolled(i);
    }

    public static void mouseMoved(int i, int i1) {
        mul.mouseMoved(i, i1);
    }

    public static void touchUp(int i, int i1, int i2, int i3) {

        mul.touchUp(i, i1, i2, i3);
    }

    public static void touchDragged(int i, int i1, int i2) {
        mul.touchDragged(i, i1, i2);
    }

    public static FileHandle[] getLocaleDir() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            //	System.out.println(System.getProperty("user.dir")
            //			+ "\\bin\\languages\\");
            return Gdx.files.absolute(
                    System.getProperty("user.dir") + "/res/languages").list();

        } else if (Gdx.app.getType() == Application.ApplicationType.Android) {
            return Gdx.files.internal("languages/").list();
        }
        return null;
    }

    public static void debug(Object s) {
        if (isDebug()) {
            Log(LogLevel.FIRST, "[DEBUG]", s.toString());
        }
    }

    public static void addCamera(GCamera cam) {
        cameras.add(cam);
    }

    public static void error(String s) {
        Log(LogLevel.FIRST, "[ERROR]", s);
    }

    public static void setDraw2D(boolean b) {
        if (b)
            FLAGS |= DRAW_2D;
        else
            FLAGS ^= DRAW_2D;
    }

    public static void setDraw3D(boolean b) {
        if (b)
            FLAGS |= DRAW_3D;
        else
            FLAGS ^= DRAW_3D;
    }

    public static void logException(Exception e) {
        error(e.getMessage());
        for (final StackTraceElement el : e.getStackTrace()) {
            error(el.toString());
        }
    }

    public static void halt() {
        Gdx.app.exit();
    }
}
