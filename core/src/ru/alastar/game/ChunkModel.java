package ru.alastar.game;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;
import ru.alastar.game.components.BaseComponent;
import ru.alastar.game.components.MeshRenderer;
import ru.alastar.game.components.Rigidbody;
import ru.alastar.game.components.Rigidbody3D;
import ru.alastar.game.physics.PShape;
import ru.alastar.game.physics.PhysicsUtils;
import ru.alastar.utils.FileManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mick on 26.04.15.
 */
public class ChunkModel extends GameObject {

    public Pixmap texture;
    private final Vector3 tmpV = new Vector3();
    private float[] vertices;
    public boolean updatePhysicShape = false;

    public ChunkModel(Model m, String tag) {
        super(m, tag, "chunkModel");
    }

    public ChunkModel() {
        super();
    }

    @Override
    public void saveTo(OutputStream out) throws IOException {

        FileManager.putInt(out, (GOType.Terrain.ordinal()));
        FileManager.putInt(out, this.flags);

        Vector3 pos = new Vector3();
        this.getTransform().getTranslation(pos);
        Quaternion q = new Quaternion();
        this.getTransform().getRotation(q);


        FileManager.putFloat(out, pos.x);
        FileManager.putFloat(out, pos.y);
        FileManager.putFloat(out, pos.z);
        FileManager.putFloat(out, q.x);
        FileManager.putFloat(out, q.y);
        FileManager.putFloat(out, q.z);
        FileManager.putFloat(out, q.w);

        Vector3 scl = new Vector3();
        this.getTransform().getScale(scl);

        FileManager.putFloat(out, scl.x);

        FileManager.putFloat(out, scl.y);

        FileManager.putFloat(out, scl.z);


        writeMeshMap(out);
        writeTexture(out);

        SaveComponents(out);

    }

    public static ChunkModel readTerrain(FileInputStream in) {
        Quaternion q = new Quaternion();
        Vector3 v = new Vector3();
        Vector3 scl = new Vector3();
        int flags = FileManager.peekInt(in);
        v.x = FileManager.peekFloat(in);
        v.y = FileManager.peekFloat(in);
        v.z = FileManager.peekFloat(in);
        q.x = FileManager.peekFloat(in);
        q.y = FileManager.peekFloat(in);
        q.z = FileManager.peekFloat(in);
        q.w = FileManager.peekFloat(in);
        scl.x = FileManager.peekFloat(in);
        scl.y = FileManager.peekFloat(in);
        scl.z = FileManager.peekFloat(in);

        return Compare(flags, v, q, scl, readMeshMap(in), buildTexture(in), in);
    }

    private static Pixmap buildTexture(FileInputStream in) {
        byte[] bytes = new byte[4];

        //in.read(bytes);
        int width = FileManager.peekInt(in);

        //in.read(bytes);
        int height = FileManager.peekInt(in);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (int x = 0; x < pixmap.getWidth(); ++x) {
            for (int y = 0; y < pixmap.getHeight(); ++y) {
                // in.read(bytes);
                int pix = FileManager.peekInt(in);
                pixmap.drawPixel(x, y, pix);

            }
        }

        return pixmap;
    }

    private void writeTexture(OutputStream out) {
        FileManager.putInt(out, texture.getWidth());
        FileManager.putInt(out, texture.getHeight());

        for (int x = 0; x < texture.getWidth(); ++x) {
            for (int y = 0; y < texture.getHeight(); ++y) {
                FileManager.putInt(out, texture.getPixel(x, y));
            }
        }
    }

    private static ChunkModel Compare(int flags, Vector3 pos, Quaternion q,
                                      Vector3 scl, TerrainChunk chunk, Pixmap tex, FileInputStream in) {
        ChunkModel r = new ChunkModel(Terrain.buildModel(chunk.width, chunk.height, chunk.vertices, chunk.indices), "chunk");
        int amt = FileManager.peekInt(in); //components size
        for (int i = 0; i < amt; ++i) {
            r.addComponent(BaseComponent.buildComponent(in, r));
        }
        r.setFlags(flags);
        r.setTexture(tex);
        r.setPosition(pos);
        r.setRotation(q);
        r.scale(scl);
        return r;
    }

    private void setTexture(Pixmap tex) {
        this.texture = tex;
    }

    private static TerrainChunk readMeshMap(FileInputStream in) {
        byte[] bytes = new byte[4];
        int vertsLength = 0, indsLength = 0;
        float[] verts = null;
        short[] inds = null;

        vertsLength = FileManager.peekInt(in);
        verts = new float[vertsLength];

        // Verts of chunk
        for (int i = 0; i < vertsLength; ++i) {
            verts[i] = FileManager.peekFloat(in);
        }
        indsLength = FileManager.peekInt(in);
        inds = new short[indsLength];

        // Indicies of chunk
        for (int i = 0; i < indsLength; ++i) {
            inds[i] = FileManager.peekShort(in);
        }
        return new TerrainChunk(32, 32, 9, verts, inds);
    }

    public void updateTransform(Matrix4 transform, Vector3 newPos, Quaternion newRotation, Vector3 newScale) {
        this.updateParentTransform(newPos, newRotation, newScale);
    }

    private void writeMeshMap(OutputStream out) {
        byte[] bytes = new byte[4];
        float[] vertices = new float[this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().meshPart.mesh.getNumVertices() * Engine.vertexSize];
        this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().meshPart.mesh.getVertices(vertices);
        short[] indices = new short[this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().meshPart.mesh.getNumIndices()];
        this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().meshPart.mesh.getIndices(indices);
        FileManager.putInt(out, vertices.length);
        // Verts of chunk
        int idx = 0;

        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 33; j++) {
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
                FileManager.putFloat(out, vertices[idx++]);
            }
        }

        FileManager.putInt(out, indices.length);

        // Indices of chunk
        for (short indice : indices) {
            FileManager.putShort(out, indice);
        }
    }

    public void calculateNormals() {
        float[] vertices = new float[this.getMesh().getNumVertices() * Engine.vertexSize];
        short[] indices = new short[this.getMesh().getNumIndices()];
        this.getMesh().getVertices(vertices);
        this.getMesh().getIndices(indices);
        TerrainChunk.calcNormals(indices, vertices);
        this.getMesh().updateVertices(0, vertices);
    }

    public Mesh getMesh() {
        return this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().meshPart.mesh;
    }

    public void setMaterial(Material standardMaterial) {
        this.getComponent(MeshRenderer.class).getModel().nodes.first().parts.first().material = standardMaterial;
        if (!((TextureAttribute) standardMaterial.get(TextureAttribute.Diffuse)).textureDescription.texture.getTextureData().isPrepared()) {
            ((TextureAttribute) standardMaterial.get(TextureAttribute.Diffuse)).textureDescription.texture.getTextureData().prepare();
        }
        this.texture = ((TextureAttribute) standardMaterial.get(TextureAttribute.Diffuse)).textureDescription.texture.getTextureData().consumePixmap();
    }

    @Override
    public void pool() {
        super.pool();
        this.texture.dispose();
    }

    public float[] getVertices() {
        vertices = new float[getMesh().getNumVertices()];
        this.getMesh().getVertices(vertices);
        return vertices;
    }

    public void updateShape() {
        if (this.haveComponent(Rigidbody.class)) {
            ((Rigidbody3D)this.getComponent(Rigidbody.class)).setShape(PhysicsUtils.getShapeFor(this, PShape.TERRAIN));
            updatePhysicShape = false;
        }
    }
}
