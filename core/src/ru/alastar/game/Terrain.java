package ru.alastar.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.alastar.Engine;
import ru.alastar.game.components.Component;
import ru.alastar.game.components.GCamera;
import ru.alastar.game.components.MeshRenderer;
import ru.alastar.game.components.Rigidbody;
import ru.alastar.game.physics.PShape;
import ru.alastar.game.physics.PhysicsUtils;
import ru.alastar.graphics.GEnvironment;
import ru.alastar.utils.FileManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


public class Terrain extends GameObject implements RenderableProvider{

    public ChunkModel[][] chunks;
    public boolean needsRecalculate = false;
    private int j, i;

    public Terrain(int x, int y, Vector3 pos) {
        super("terrain");

        transform.setToTranslation(pos);
        Pixmap pix;
        chunks = new ChunkModel[x][y];
        float[][] arr = new float[32][32];

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                chunks[i][j] = new ChunkModel(buildModel(32, arr, i, j, pos.y), "chunk" + i + " " + j);
                Material standardMaterial = new Material();
                standardMaterial.id = "mtl" + i + " " + j;
                pix = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
                pix.setColor(Color.WHITE);
                pix.fill();
                standardMaterial.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture(pix)));
                chunks[i][j].setMaterial(standardMaterial);
                chunks[i][j].setPosition(new Vector3(pos.x + i * 32, pos.y, pos.z + j * 32));
                //DebugChunk(chunks[i][j]);
            }
        }
    }

    private void DebugChunk(ChunkModel chunkModel) {
        float[] vertices;
        for (i = 0; i < chunkModel.getComponent(MeshRenderer.class).getModel().nodes.size; ++i) {
            System.out.println("Node: " + chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).id);
            for (j = 0; j < chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).parts.size; ++j) {
                System.out.println("MeshPart: " + chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).parts.get(j).meshPart.id);
                System.out.println("Material: " + chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).parts.get(j).material.id);
                vertices = new float[chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).parts.get(j).meshPart.mesh.getNumVertices() * 9];
                chunkModel.getComponent(MeshRenderer.class).getModel().nodes.get(i).parts.get(j).meshPart.mesh.getVertices(vertices);
                System.out.println("Mesh begin: " + vertices[0] + " : " + vertices[2]);

            }
        }
    }

    public Terrain() {
        super("terrain");
    }

    @Override
    public void render(ModelBatch modelBatch,Camera cam,  GEnvironment env) {
        for (i = 0; i < chunks.length; ++i) {
            for (j = 0; j < chunks.length; ++j) {
                if (GCamera.isVisible(cam, chunks[i][j])) {
                        modelBatch.render(chunks[i][j].getModel(), env);
                }
            }
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for(i = 0; i < chunks.length;++i){
            for(j = 0; j < chunks.length; ++j){
                renderables.add(chunks[i][j].getModel().getRenderable(new Renderable()));
            }
        }
    }

    @Override
    public void updateTransform() {
        for (i = 0; i < chunks.length; ++i) {
            for (j = 0; j < chunks.length; ++j) {
                chunks[i][j].updateTransform(this.transform, position, rotation, scale);
            }
        }
        this.getTransform().set(position, rotation, scale);
    }

    private static Model buildModel(int i1, float[][] arr, int x, int y, float posY) {
        ModelBuilder mb = new ModelBuilder();
        TerrainChunk chunk = new TerrainChunk(32, Engine.vertexSize, arr);
        mb.begin();
        Material material = new Material();
        Mesh mesh = new Mesh(
                true,
                chunk.vertices.length,
                chunk.indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3,
                        "a_position"), new VertexAttribute(
                VertexAttributes.Usage.Normal, 3,
                ShaderProgram.NORMAL_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4,
                        "a_color", 2), new VertexAttribute(
                VertexAttributes.Usage.TextureCoordinates, 2,
                ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        mesh.setVertices(chunk.vertices);
        mesh.setIndices(chunk.indices);

        mb.part("terrainChunk " + x + " " + y, mesh,
                GL30.GL_TRIANGLES, material);
        return mb.end();
    }

    public static Model buildModel(short width, short height, float[] vertices, short[] indices) {
        ModelBuilder mb = new ModelBuilder();
        Material material = new Material();
        TerrainChunk chunk = new TerrainChunk(width, height, Engine.vertexSize, vertices, indices);
        mb.begin();
        material = new Material();
        material.id = "mtl" + vertices[0] / width + " " + vertices[2] / height;
        Mesh mesh = new Mesh(
                true,
                chunk.vertices.length,
                chunk.indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3,
                        "a_position"), new VertexAttribute(
                VertexAttributes.Usage.Normal, 3,
                ShaderProgram.NORMAL_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4,
                        "a_color", 2), new VertexAttribute(
                VertexAttributes.Usage.TextureCoordinates, 2,
                ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        mesh.setVertices(chunk.vertices);
        mesh.setIndices(chunk.indices);

        mb.part("terrainChunk " + vertices[0] / width + " " + vertices[2] / height, mesh,
                GL30.GL_TRIANGLES, material);
        return mb.end();
    }

    public static GameObject readTerrain(FileInputStream in) {
        byte[] bytes = new byte[4];
        Terrain terra = null;

        Quaternion q = new Quaternion();
        Vector3 v = new Vector3();
        Vector3 scl = new Vector3();

        //byte[] bytes = new byte[4];
        //in.read(bytes);
        //int tagBytes = FileManager.peekInt(in);

        //System.out.println(tagBytes);

        //bytes = new byte[tagBytes];
        //in.read(bytes);
        int flags = FileManager.peekInt(in);
        String tag = FileManager.peekString(in);

        //bytes = new byte[4];
        //in.read(bytes);
        //int modelNameBytes = ByteBuffer.wrap(bytes).getInt();

        //bytes = new byte[modelNameBytes];
        //in.read(bytes);
        String modelName = FileManager.peekString(in);

        //bytes = new byte[4];
        //in.read(bytes);
        //int texNameBytes = ByteBuffer.wrap(bytes).getInt();

        //bytes = new byte[texNameBytes];
        //in.read(bytes);
        String texName = FileManager.peekString(in);

        //bytes = new byte[4];
        //in.read(bytes);
        v.x = FileManager.peekFloat(in);
        //System.out.println("x pos " + v.x);

        //in.read(bytes);
        v.y = FileManager.peekFloat(in);
        //System.out.println("y pos " + v.y);

        //in.read(bytes);
        v.z = FileManager.peekFloat(in);
        //System.out.println("z pos " + v.z);

        //in.read(bytes);
        q.x = FileManager.peekFloat(in);
        //System.out.println("x q " + q.x);

        //in.read(bytes);
        q.y = FileManager.peekFloat(in);
        //System.out.println("x y " + q.y);

        //	in.read(bytes);
        q.z = FileManager.peekFloat(in);
        //System.out.println("x z " + q.z);

        //in.read(bytes);
        q.w = FileManager.peekFloat(in);
        //System.out.println("x w " + q.w);

        //in.read(bytes);
        scl.x = FileManager.peekFloat(in);
        //System.out.println(" scl x " + scl.x);

        //in.read(bytes);
        scl.y = FileManager.peekFloat(in);
        //System.out.println("scl y " + scl.y);


        //	in.read(bytes);

        scl.z = FileManager.peekFloat(in);
        //System.out.println("scl z " + scl.z);


        //in.read(bytes);
        int x = FileManager.peekInt(in);

        //in.read(bytes);
        int y = FileManager.peekInt(in);

        terra = new Terrain();
        terra.setFlags(flags);
        terra.chunks = new ChunkModel[x][y];
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                terra.chunks[x][y] = ChunkModel.readTerrain(in);
            }
        }
        return terra;
    }

    @Override
    public Matrix4 getTransform() {
        return transform;
    }

    @Override
    public void saveTo(OutputStream out) throws IOException {
        byte[] bytes = ByteBuffer.allocate(4).putInt(GOType.Terrain.ordinal()).array();
        out.write(bytes);

        Vector3 pos = new Vector3();
        this.getTransform().getTranslation(pos);
        Quaternion q = new Quaternion();
        this.getTransform().getRotation(q);

        bytes = ByteBuffer.allocate(4).putFloat(pos.x).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(pos.y).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(pos.z).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(q.x).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(q.y).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(q.z).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(q.w).array();
        out.write(bytes);

        Vector3 scl = new Vector3();
        this.getTransform().getScale(scl);

        bytes = ByteBuffer.allocate(4).putFloat(scl.x).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(scl.y).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(scl.z).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(this.chunks.length).array();
        out.write(bytes);

        bytes = ByteBuffer.allocate(4).putFloat(this.chunks.length).array();
        out.write(bytes);

        for (ChunkModel[] chunk : chunks) {
            for (int j = 0; j < chunks.length; ++j) {
                chunk[j].saveTo(out);
            }
        }

    }

    @Override
    public void addComponent(Component c) {
        if (c instanceof Rigidbody) {
            Rigidbody r = null;
            for (i = 0; i < chunks.length; ++i) {
                for (j = 0; j < chunks.length; ++j) {
                    r = PhysicsUtils.create3DRigidBody(chunks[i][j], 0.0f, PShape.TERRAIN, true, true);
                    chunks[i][j].addComponent(r);
                    r.setOwner(chunks[i][j]);
                }
            }
        } else {
            this.components.add(c);
            c.setOwner(this);
        }
    }

    @Override
    public Vector3 getPosition(Vector3 chain) {
        this.getTransform().getTranslation(chain);
        return chain;
    }

    @Override
    public Vector3 getScale(Vector3 chain) {
        this.getTransform().getScale(chain);
        return chain;
    }

    public void calculate() {
        for (i = 0; i < chunks.length; ++i) {
            for (j = 0; j < chunks.length; ++j) {
                chunks[i][j].calculateNormals();
                if (chunks[i][j].updatePhysicShape)
                    chunks[i][j].updateShape();
            }
        }
        needsRecalculate = false;
    }

    @Override
    public void dispose() {
        for (i = 0; i < chunks.length; ++i) {
            for (j = 0; j < chunks.length; ++j) {
                chunks[i][j].dispose();
            }
        }
    }

    public static float[] getHeightMap(GameObject gameObject) {
        ChunkModel cm = (ChunkModel) gameObject;
        return cm.getVertices();
    }

    public static float getMinHeight(GameObject gameObject) {
        ChunkModel cm = (ChunkModel) gameObject;
        float[] vertices = cm.getVertices();
        float min = vertices[1];
        for (int j = 9; j < vertices.length; j += 9) {
            if (min > vertices[j + 1])
                min = vertices[j + 1];
        }
        return min;
    }

    public static float getMaxHeight(GameObject gameObject) {
        ChunkModel cm = (ChunkModel) gameObject;
        float[] vertices = cm.getVertices();
        float max = vertices[1];
        for (int j = 9; j < vertices.length; j += 9) {
            if (max < vertices[j + 1])
                max = vertices[j + 1];
        }
        return max;
    }
}
